package com.milk.milkanalysis.dto;

import lombok.Data;

@Data
public class MilkDataRequest {
    private String userId;     // mobile number
    private String session;    // morning or evening
    private String date;       // yyyy-MM-dd
    private double liters;
    private double rate;
    private double fat;
    private double snf;
}
