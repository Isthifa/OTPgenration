package com.example.otpgenrator.generator;

import com.example.otpgenrator.encrypt.OtpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class OtpService {
    private static final Logger logger = LoggerFactory.getLogger(OtpService.class);
    private Map<String, OTPEntry> otpStore = new HashMap<>();

    public String generateOTP(String countryCode, String phoneNumber, LocalDate dateOfBirth) {
        String otp = OtpUtils.generateOTP(countryCode, phoneNumber, dateOfBirth);
        String key = countryCode + phoneNumber;
        otpStore.put(key, new OTPEntry(otp, LocalDateTime.now().plusSeconds(30),false)); // OTP valid for 5 minutes
        logger.info("Generated OTP for {}: {}", key, otp);
        logger.info("Current OTP store: {}", otpStore);
        return otp;
    }

    public boolean verifyOTP(String countryCode, String phoneNumber, String otp) {
        String key = countryCode + phoneNumber;
        logger.info("Current OTP store before verification: {}", otpStore);
        OTPEntry entry = otpStore.get(key);
        logger.info("Verifying OTP for {}: Stored OTP: {}, Received OTP: {}", key, entry != null ? entry.otp : "null", otp);
        if (entry != null) {
            logger.info("OTP expiry time: {}, Current time: {}", entry.expiryTime, LocalDateTime.now());
        }
        if (entry != null && entry.otp.equals(otp) && LocalDateTime.now().isBefore(entry.expiryTime) && entry.expired==false) {
            otpStore.remove(key); // Remove OTP after successful verification

            logger.info("OTP verified successfully for {}", key);
            return true;
        }
        logger.info("OTP verification failed for {}", key);
        return false;
    }

    public void logOtpStore() {
        logger.info("Current OTP store: {}", otpStore);
    }

    private static class OTPEntry {
        String otp;
        LocalDateTime expiryTime;

        boolean expired;

        OTPEntry(String otp, LocalDateTime expiryTime,boolean expired) {
            this.otp = otp;
            this.expiryTime = expiryTime;
            this.expired=expired;
        }

        @Override
        public String toString() {
            return "OTPEntry{otp='" + otp + "', expiryTime=" + expiryTime + '}';
        }
    }
}