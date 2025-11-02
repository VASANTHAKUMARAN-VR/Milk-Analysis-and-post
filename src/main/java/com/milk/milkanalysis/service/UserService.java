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
        var userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User u = userOpt.get();
            u.setEnabled(true);
            return userRepository.save(u);
        }
        return null;
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean existsByMobile(String mobile) {
        return userRepository.existsByMobileNumber(mobile);
    }

    public User findByEmailOrMobile(String identifier) {
        var opt = userRepository.findByEmailOrMobileNumber(identifier, identifier);
        return opt.orElse(null);
    }

    public void updatePassword(String email, String newPassword) {
        var opt = userRepository.findByEmail(email);
        if (opt.isPresent()) {
            User u = opt.get();
            u.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(u);
        }
    }

    public boolean deleteUserByEmailOrMobile(String identifier) {
        var userOpt = userRepository.findByEmailOrMobileNumber(identifier, identifier);
        if (userOpt.isPresent()) {
            userRepository.delete(userOpt.get());
            return true;
        }
        return false;
    }

}
