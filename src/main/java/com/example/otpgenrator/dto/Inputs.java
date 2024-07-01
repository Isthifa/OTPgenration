package com.example.otpgenrator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Inputs {
    private String countryCode;
    private String phoneNumber;
    private String dateOfBirth;
}
