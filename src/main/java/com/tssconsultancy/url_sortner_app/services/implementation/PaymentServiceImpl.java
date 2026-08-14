package com.tssconsultancy.url_sortner_app.services.implementation;

import com.tssconsultancy.url_sortner_app.constants.SystemConfigConstants;
import com.tssconsultancy.url_sortner_app.dtos.PageResponse;
import com.tssconsultancy.url_sortner_app.dtos.payment.PaymentResponseDto;
import com.tssconsultancy.url_sortner_app.entities.Payment;
import com.tssconsultancy.url_sortner_app.entities.Url;
import com.tssconsultancy.url_sortner_app.entities.User;
import com.tssconsultancy.url_sortner_app.enums.PaymentStatus;
import com.tssconsultancy.url_sortner_app.enums.PaymentType;
import com.tssconsultancy.url_sortner_app.enums.UrlStatus;
import com.tssconsultancy.url_sortner_app.exceptions.derived.UrlNotFoundException;
import com.tssconsultancy.url_sortner_app.exceptions.derived.UserNotFoundException;
import com.tssconsultancy.url_sortner_app.mapper.PaymentMapper;
import com.tssconsultancy.url_sortner_app.repositories.PaymentRepository;
import com.tssconsultancy.url_sortner_app.repositories.UrlRepository;
import com.tssconsultancy.url_sortner_app.repositories.UserRepository;
import com.tssconsultancy.url_sortner_app.services.interfaces.IPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements IPaymentService {
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final UrlRepository urlRepository;

    private final SystemConfigServiceImpl systemConfigService;

    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public PaymentResponseDto initiatePayment(Long userId, Long urlId, PaymentType paymentType) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Url url = urlRepository.findById(urlId)
                .orElseThrow(() -> new UrlNotFoundException(urlId));

        Double amount = getPriceForFeature(paymentType);

        Payment payment = new Payment();
        payment.setPaymentType(paymentType);
        payment.setUrl(url);
        payment.setUser(user);
        payment.setAmount(amount);
        payment.setPaymentStatus(PaymentStatus.PENDING);

        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toResponse(savedPayment);
    }

    @Override
    @Transactional
    public PaymentResponseDto processPayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with ID: " + paymentId));

        if (payment.getPaymentStatus() != PaymentStatus.PENDING) {
            throw new RuntimeException("Only PENDING payments can be processed");
        }

        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setCompletedAt(LocalDateTime.now());

        fulfillPaymentBenefits(payment);

        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.toResponse(savedPayment);
    }

    private void fulfillPaymentBenefits(Payment payment) {
        PaymentType type = payment.getPaymentType();
        User user = payment.getUser();
        Url url = payment.getUrl();

        if (type == PaymentType.URL_RENEWAL && url != null) {
            int extraVisits = systemConfigService.getIntConfig(
                    SystemConfigConstants.RENEWAL_VISITS_GRANTED,
                    SystemConfigConstants.FALLBACK_RENEWAL_VISITS_GRANTED);

            int currentLimit = url.getVisitLimit() != null ? url.getVisitLimit() : 0;
            int currentRemaining = url.getRemainingVisits() != null ? url.getRemainingVisits() : 0;

            url.setVisitLimit(currentLimit + extraVisits);
            url.setRemainingVisits(currentRemaining + extraVisits);
            url.setUrlStatus(UrlStatus.ACTIVE);
            urlRepository.save(url);
        } else if (type == PaymentType.URL_SLOT_PURCHASE && user != null) {
            int currentSlots = user.getRemainingUrlSlots() != null ? user.getRemainingUrlSlots() : 0;
            user.setRemainingUrlSlots(currentSlots + 1);
            userRepository.save(user);
        } else if (type == PaymentType.CUSTOM_ALIAS && url != null) {
            url.setUrlStatus(UrlStatus.ACTIVE);
            urlRepository.save(url);
        }
    }

    @Override
    @Transactional
    public PaymentResponseDto cancelPayment(Long paymentId, Long userId) {
        Payment payment = paymentRepository.findByPaymentIdAndUserUserId(paymentId, userId)
                .orElseThrow(() -> new RuntimeException("Payment not found or Unauthorized access to payment record"));

        if (payment.getPaymentStatus() != PaymentStatus.PENDING) {
            throw new RuntimeException("Cannot cancel a completed or failed payment");
        }

        payment.setPaymentStatus(PaymentStatus.CANCELLED);
        payment.setCompletedAt(LocalDateTime.now());
        Payment savedPayment = paymentRepository.save(payment);

        return paymentMapper.toResponse(savedPayment);
    }

    @Override
    public PaymentResponseDto getPaymentById(Long paymentId, Long userId) {
        Payment payment = paymentRepository.findByPaymentIdAndUserUserId(paymentId, userId)
                .orElseThrow(() -> new RuntimeException("Payment not found or Unauthorized access to payment record"));

        return paymentMapper.toResponse(payment);
    }

    @Override
    public PageResponse<PaymentResponseDto> getUserPayments(Long userId, Pageable pageable) {
        Page<Payment> paymentPage = paymentRepository.findAllByUserUserId(userId, pageable);

        List<PaymentResponseDto> content = paymentPage.getContent().stream()
                .map(paymentMapper::toResponse)
                .toList();

        return PageResponse.<PaymentResponseDto>builder()
                .content(content)
                .page(paymentPage.getNumber())
                .size(paymentPage.getSize())
                .totalElements(paymentPage.getTotalElements())
                .totalPages(paymentPage.getTotalPages())
                .last(paymentPage.isLast())
                .build();
    }

    private Double getPriceForFeature(PaymentType paymentType) {
        String configKey;
        Double fallbackPrice;

        switch (paymentType) {
            case URL_RENEWAL:
                configKey = SystemConfigConstants.RENEWAL_FEE;
                fallbackPrice = SystemConfigConstants.FALLBACK_RENEWAL_FEE;
                break;
            case URL_SLOT_PURCHASE:
                configKey = SystemConfigConstants.PRICE_PER_ADDITIONAL_SLOT;
                fallbackPrice = SystemConfigConstants.FALLBACK_PRICE_PER_ADDITIONAL_SLOT;
                break;
            case CUSTOM_ALIAS:
                configKey = SystemConfigConstants.PRICE_CUSTOM_ALIAS;
                fallbackPrice = SystemConfigConstants.FALLBACK_PRICE_CUSTOM_ALIAS;
                break;
            case QR_CODE:
                configKey = SystemConfigConstants.PRICE_QR_CODE;
                fallbackPrice = SystemConfigConstants.FALLBACK_PRICE_QR_CODE;
                break;
            default:
                throw new IllegalArgumentException("Unknown payment type: " + paymentType);
        }

        Double price = systemConfigService.getDoubleConfig(configKey, fallbackPrice);

        return price;
    }

    // TODO: give url slots according to the user's need and pricing according to it
}