package com.milk.milkanalysis.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "milk_data")
public class MilkData {

    @Id
    private String id;

    private String userId;   // mobile number (acts as user ID)
    private String session;  // morning / evening
    private LocalDate date;

    private double liters;
    private double rate;
    private double fat;
    private double snf;
    private double amount;
}
