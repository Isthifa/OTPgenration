package com.example.otpgenrator.controller;

import com.example.otpgenrator.generator.OtpService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/otp")
@Slf4j
public class OtpController {

    Logger logger = LoggerFactory.getLogger(OtpController.class);
    @Autowired
    private OtpService otpService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateOTP(@RequestBody OTPGenerationRequest request) {
        try {
            String encryptedOTP = otpService.generateOTP(request.getCountryCode(), request.getPhoneNumber(), request.getDateOfBirth());
            return ResponseEntity.ok(encryptedOTP);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to generate OTP: " + e.getMessage());
        }


    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOTP(@RequestBody OTPVerificationRequest request) {
        logger.info("Received verification request: countryCode={}, phoneNumber={}, otp={}",
                request.getCountryCode(), request.getPhoneNumber(), request.getOtp());
        otpService.logOtpStore();
        boolean isValid = otpService.verifyOTP(request.getCountryCode(), request.getPhoneNumber(), request.getOtp());
        if (isValid) {
            return ResponseEntity.ok("OTP verified successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP. Please check the logs for more information.");
        }
    }

    // Request DTOs
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class OTPGenerationRequest {
        private String countryCode;
        private String phoneNumber;
        private LocalDate dateOfBirth;

        // Getters and setters
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class OTPVerificationRequest {
        private String countryCode;
        private String phoneNumber;
        private String otp;

        // Getters and setters
    }
}