package com.milk.milkanalysis.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "market_posts")
public class MarketPost {
    @Id
    private String id;
    private String userName;
    private String mobileNumber;
    private String description;
    private String location;
    private String imageUrl;
    private LocalDate date;
    private PostCategory category;  // Enum type
}
