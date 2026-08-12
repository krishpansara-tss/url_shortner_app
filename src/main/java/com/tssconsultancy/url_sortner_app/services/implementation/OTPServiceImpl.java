package com.tssconsultancy.url_sortner_app.services.implementation;

import com.tssconsultancy.url_sortner_app.services.interfaces.OTPService;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class OTPServiceImpl implements OTPService {

    private final SecureRandom random = new SecureRandom();

    @Override
    public String generateOtp() {
        int number = random.nextInt(1000000);
        return String.format("%06d", number);
    }
}
