package com.milk.milkanalysis.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "buy_records")
public class BuyRecord {

    @Id
    private String id;

    private String userId;        // Buyer’s user ID
    private String productName;   // e.g. MAATU_SAANAM, GRASS_FEED, MILK_PRODUCT, OTHER
    private double price;         // Price of the product
    private LocalDate date;       // Date of buying
}
