package com.milk.milkanalysis.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String usernameOrMobileOrEmail;
    private String password;
    // getters/setters

    @Data
    public static class CowPurchaseRequest {
        private String userId;
        private String salesName;
        private int quantity;
        private double totalAmount;
        private String purchasePlace;
        private String date; // format: YYYY-MM-DD
    }
}
