package com.milk.milkanalysis.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cow_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CowRequest {

    @Id
    private String id;

    private String userId;          // Who created the request
    private String userName;
    private String mobileNumber;
    private String cowBreedNeeded;
    private double milkLitersNeeded;
    private String purpose;
    private String location;
    private String additionalNotes;
}
