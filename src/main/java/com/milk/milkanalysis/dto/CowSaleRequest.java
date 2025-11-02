package com.milk.milkanalysis.dto;

import lombok.Data;

@Data
public class CowSaleRequest {
    private String userId;
    private String date;      // YYYY-MM-DD format
    private String buyerName;
    private int quantity;
    private double totalAmount;
}
