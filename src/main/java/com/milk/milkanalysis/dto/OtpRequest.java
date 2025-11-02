package com.milk.milkanalysis.dto;

import lombok.Data;

@Data
public class OtpRequest {
    private String email;
    private String code;
    // getters/setters
}
