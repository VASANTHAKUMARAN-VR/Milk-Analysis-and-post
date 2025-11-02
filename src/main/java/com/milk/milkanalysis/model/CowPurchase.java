package com.milk.milkanalysis.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "cow_purchases")
public class CowPurchase {

    @Id
    private String id;

    private String userId;         // user mobile number or id
    private LocalDate date;        // purchase date
    private String salesmanName;   // who sold the cow
    private int quantity;          // number of cows purchased
    private double totalAmount;    // total cost
    private String purchasePlace;  // where purchase happened

    public static class CowRequest {
    }
}
