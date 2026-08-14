package com.tssconsultancy.url_sortner_app.services.implementation;

import com.tssconsultancy.url_sortner_app.entities.OTPModel;
import com.tssconsultancy.url_sortner_app.repositories.OTPRepository;
import com.tssconsultancy.url_sortner_app.services.interfaces.INotificationService;
import com.tssconsultancy.url_sortner_app.services.interfaces.IOTPService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service("email")
@RequiredArgsConstructor
public class EmailNotifier implements INotificationService {
    private final JavaMailSender mailSender;
    private final IOTPService IOTPService;
    private final OTPRepository otpRepository;

    @Override
    public void sendMessage(String to, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(to);
        mailMessage.setText(message);

        mailSender.send(mailMessage);

        System.out.println("Receiver : " + to + " | Message (Sent ON Email) : " + message);
    }

    @Override
    public void verifyOtp(String email, String otp) {
        OTPModel otpObject = otpRepository.findTopByEmailAndUsedFalseOrderByCreatedAtDesc(email);

        if(otpObject == null){
            throw new RuntimeException("OTP not sent to your account");
        }

        if(otpObject.getExpiryTime().isBefore(LocalDateTime.now())){
            throw new RuntimeException("OTP has been expired");
        }

        if(otpObject.isUsed()){
            throw new RuntimeException("OTP have already used the otp");
        }

        if(!otpObject.getVerificationCode().equals(otp)){
            throw new RuntimeException("OTP didn't matched");
        }else{
            System.out.println("OTP MATCHED");
            otpObject.setUsed(true);
            otpRepository.save(otpObject);
        }
    }

    @Override
    public void sendOtp(String email) {
        LocalDateTime windowStart = LocalDateTime.now().minusMinutes(5);

        long attempts = otpRepository.countByEmailAndCreatedAtAfter(email, windowStart);

        if (attempts >= 3) {
            throw new RuntimeException("Too many OTP requests. Try again later.");
        }

        String otp = IOTPService.generateOtp();

        sendMessage(email, otp);

        OTPModel otpObject = new OTPModel();
        otpObject.setEmail(email);
        otpObject.setVerificationCode(otp);
        otpObject.setCreatedAt(LocalDateTime.now());
        otpObject.setExpiryTime(LocalDateTime.now().plusMinutes(2));
        otpObject.setUsed(false);

        otpRepository.save(otpObject);
    }
}
