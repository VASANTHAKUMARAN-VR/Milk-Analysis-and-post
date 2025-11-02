package com.milk.milkanalysis.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "cow_sales")
public class CowSale {

    @Id
    private String id;

    private String userId;       // seller ID (user)
    private LocalDate date;      // sale date
    private String buyerName;    // who bought the cow
    private int quantity;        // number of cows sold
    private double totalAmount;  // total sale value
}
