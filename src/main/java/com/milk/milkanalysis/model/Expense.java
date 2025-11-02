package com.milk.milkanalysis.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Expense {
    @Id
    private String id;

    private String userId;
    private double feedCost;
    private double powderCost;
    private double labourCost;
    private double medicalCost;
    private double machineryCost;
}
