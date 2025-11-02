package com.milk.milkanalysis.dto;

import lombok.Data;

@Data
public class DeleteAccountRequest {
    private String identifier; // can be email or mobile
}
