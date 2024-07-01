package com.example.otpgenrator;

import com.example.otpgenrator.encrypt.OtpUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
class OtpgenratorApplicationTests {

    @Test
    void contextLoads() {
    }
    public static void main(String[] args) {
        String countryCode = "91";
        String phoneNumber = "9876543210";
        LocalDate dateOfBirth = LocalDate.of(1990, 1, 1);

        String otp = OtpUtils.generateOTP(countryCode, phoneNumber, dateOfBirth);
        System.out.println("Generated OTP: " + otp);
    }



}
