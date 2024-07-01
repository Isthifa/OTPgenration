package com.example.otpgenrator.encrypt;

import java.security.MessageDigest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


    public class  OtpUtils{


            private static final String SECRET_KEY = "MySecretKey12345"; // In practice, use a secure method to manage this key

            public static String generateOTP(String countryCode, String phoneNumber, LocalDate dateOfBirth) {
                // Ensure country code is 2 digits
                if (countryCode.length() != 2) {
                    throw new IllegalArgumentException("Country code must be 2 digits");
                }

                // Combine the input
                String combined = countryCode + phoneNumber + dateOfBirth.format(DateTimeFormatter.BASIC_ISO_DATE);

                // Generate HMAC-SHA256 hash
                byte[] hash = generateHMAC(combined);

                // Convert the first 3 bytes of the hash to an integer
                int hashValue = ((hash[0] & 0xFF) << 16) | ((hash[1] & 0xFF) << 8) | (hash[2] & 0xFF);

                // Ensure it's 6 digits by using modulo and padding
                return String.format("%06d", hashValue % 1000000);
            }

            private static byte[] generateHMAC(String input) {
                try {
                    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
                    SecretKeySpec secret_key = new SecretKeySpec(SECRET_KEY.getBytes(), "HmacSHA256");
                    sha256_HMAC.init(secret_key);

                    return sha256_HMAC.doFinal(input.getBytes());
                } catch (Exception e) {
                    throw new RuntimeException("Failed to generate HMAC", e);
                }
            }
        }