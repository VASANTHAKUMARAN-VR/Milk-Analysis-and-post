package com.milk.milkanalysis.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "sale_records")
public class SaleRecord {

    @Id
    private String id;

    private String userId; // can be mobile number or unique user ID

    private PostCategory category; // MAATU_SAANAM, GRASS_FEED, MILK_PRODUCT, OTHER

    private double price; // sale price

    private LocalDate date; // sale date
}
