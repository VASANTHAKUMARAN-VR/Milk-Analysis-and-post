package com.milk.milkanalysis.dto;

import com.milk.milkanalysis.model.Role;
import lombok.Data;

@Data
public class SignupRequest {
    private String name;
    private String email;
    private String mobileNumber;
    private String password;
    private Role role; // optional, if omitted default to USER

    // getters & setters
}
