package com.milk.milkanalysis.dto;

import lombok.Data;

@Data
public class ExpenseRequest {
    private String userId;
    private double feedCost;
    private double powderCost;
    private double labourCost;
    private double medicalCost;
    private double machineryCost;
}
