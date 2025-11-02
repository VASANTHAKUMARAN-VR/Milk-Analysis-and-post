package com.milk.milkanalysis.dto;

import lombok.Data;

@Data
public class CowPurchaseRequest {
    private String userId;
    private String salesmanName;
    private int quantity;
    private double totalAmount;
    private String purchasePlace;
    private String date;  // Format: YYYY-MM-DD
}
