package com.milk.milkanalysis.service;

import com.milk.milkanalysis.model.Role;
import com.milk.milkanalysis.model.User;
import com.milk.milkanalysis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(String name, String email, String mobile, String rawPassword, Role role) {
        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setMobileNumber(mobile);
        u.setPassword(passwordEncoder.encode(rawPassword));
        u.setRoles(Set.of(role));
        u.setEnabled(false); // wait for OTP verification
        return userRepository.save(u);
    }

    public User enableUser(String email) {
        return userRepository.findByEmail(email)
                .map(u -> {
                    u.setEnabled(true);
                    return userRepository.save(u);
                })
                .orElse(null);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByMobile(String mobile) {
        return userRepository.existsByMobileNumber(mobile);
    }

    public User findByEmailOrMobile(String identifier) {
        return userRepository.findByEmailOrMobileNumber(identifier, identifier).orElse(null);
    }

    public void updatePassword(String email, String newPassword) {
        userRepository.findByEmail(email).ifPresent(u -> {
            u.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(u);
        });
    }

    public boolean deleteUserByEmailOrMobile(String identifier) {
        return userRepository.findByEmailOrMobileNumber(identifier, identifier)
                .map(u -> {
                    userRepository.delete(u);
                    return true;
                })
                .orElse(false);
    }
}
