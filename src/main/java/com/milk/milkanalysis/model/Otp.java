package com.milk.milkanalysis.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "otps")
public class Otp {
    @Id
    private String id;

    private String email; // tied to email
    private String code;
    private boolean used = false;
    private LocalDateTime expiryTime;

    // constructors/getters/setters
    public Otp() {}

    public Otp(String email, String code, LocalDateTime expiryTime) {
        this.email = email;
        this.code = code;
        this.expiryTime = expiryTime;
    }

    // getters/setters...
}
