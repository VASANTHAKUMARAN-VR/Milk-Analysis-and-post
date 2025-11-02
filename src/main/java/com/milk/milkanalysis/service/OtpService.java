package com.milk.milkanalysis.service;

import com.milk.milkanalysis.model.Otp;
import com.milk.milkanalysis.repository.OtpRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class OtpService {

    @Autowired
    private OtpRepository otpRepository;

    // Generate OTP and save
    public String generateAndSaveOtp(String email) {
        String code = String.format("%06d", new Random().nextInt(1_000_000));
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(10);
        Otp otp = new Otp(email, code, expiry);
        otpRepository.save(otp);
        return code;
    }

    // Validate OTP for given email
    public boolean validateOtp(String email, String code) {
        var opt = otpRepository.findTopByEmailAndUsedOrderByExpiryTimeDesc(email, false);
        if (opt.isEmpty()) return false;
        Otp found = opt.get();
        if (found.isUsed()) return false;
        if (!found.getCode().equals(code)) return false;
        if (found.getExpiryTime().isBefore(LocalDateTime.now())) return false;

        // mark as used
        found.setUsed(true);
        otpRepository.save(found);
        return true;
    }

    // ✅ Find email from OTP code (for verification without email)
    public String findEmailByOtp(String code) {
        var otpOpt = otpRepository.findByCode(code);
        if (otpOpt.isPresent() && !otpOpt.get().isUsed() &&
                otpOpt.get().getExpiryTime().isAfter(LocalDateTime.now())) {
            return otpOpt.get().getEmail();
        }
        return null;
    }
}
