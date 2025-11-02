package com.milk.milkanalysis.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;

    private String name;

    @Indexed(unique = true)
    private String email;

    @Indexed(unique = true)
    private String mobileNumber; // used as userid

    private String password; // encoded

    private Set<Role> roles;

    private boolean enabled = false; // enabled after OTP verification

    private LocalDateTime createdAt = LocalDateTime.now();

    // getters / setters / constructors
    // (use Lombok if you prefer)
    public User() {}
    // getters & setters omitted for brevity — add them or use Lombok @Data
    // ... generate getters/setters
    // I'll assume you add them or use Lombok in your project
}
