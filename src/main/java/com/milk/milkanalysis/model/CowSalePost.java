package com.milk.milkanalysis.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "cow_sale_posts")
public class CowSalePost {
    @Id
    private String id;
    private String userName;
    private String mobileNumber;
    private String description;
    private String location;
    private String imageUrl;
    private LocalDate date;
}
