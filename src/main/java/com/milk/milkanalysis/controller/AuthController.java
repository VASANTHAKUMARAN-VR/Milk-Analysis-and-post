package com.milk.milkanalysis.controller;

import com.milk.milkanalysis.dto.*;
import com.milk.milkanalysis.model.Role;
import com.milk.milkanalysis.model.User;
import com.milk.milkanalysis.service.EmailService;
import com.milk.milkanalysis.service.OtpService;
import com.milk.milkanalysis.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private UserService userService;
    @Autowired private OtpService otpService;
    @Autowired private EmailService emailService;
    @Autowired private AuthenticationManager authenticationManager;

    // signup -> send OTP to email
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest req) {
        if (userService.existsByEmail(req.getEmail())) {
            return ResponseEntity.badRequest().body("Email already in use");
        }
        if (userService.existsByMobile(req.getMobileNumber())) {
            return ResponseEntity.badRequest().body("Mobile already in use");
        }

        Role role = req.getRole() == null ? Role.USER : req.getRole();
        // create user but not enabled
        User u = userService.createUser(req.getName(), req.getEmail(), req.getMobileNumber(), req.getPassword(), role);

        // generate OTP and email
        String otp = otpService.generateAndSaveOtp(req.getEmail());
        String msg = "Your signup OTP is: " + otp + " (valid 10 minutes)";
        emailService.sendSimpleMail(req.getEmail(), "Signup OTP - Milk App", msg);

        return ResponseEntity.ok("OTP sent to email. Verify to activate account.");
    }

    @PostMapping("/verify-signup-otp")
    public ResponseEntity<?> verifySignupOtp(@RequestBody OtpRequest req) {
        // find email by OTP code
        String email = otpService.findEmailByOtp(req.getCode());
        if (email == null) {
            return ResponseEntity.badRequest().body("Invalid or expired OTP");
        }

        boolean ok = otpService.validateOtp(email, req.getCode());
        if (!ok) return ResponseEntity.badRequest().body("Invalid or expired OTP");

        User enabled = userService.enableUser(email);
        if (enabled == null) return ResponseEntity.badRequest().body("User not found");
        return ResponseEntity.ok("Account verified and enabled. You can now login.");
    }


    // login (mobile or email + password)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsernameOrMobileOrEmail(), req.getPassword()));
            User user = userService.findByEmailOrMobile(req.getUsernameOrMobileOrEmail());
            if (user == null) return ResponseEntity.badRequest().body("User not found");
            return ResponseEntity.ok("Login successful");
        } catch (Exception ex) {
            return ResponseEntity.status(401).body("Invalid credentials or account not enabled");
        }
    }

    // forgot password -> send OTP
    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody OtpRequest req) {
        // check user exists
        User user = userService.findByEmailOrMobile(req.getEmail());
        if (user == null) return ResponseEntity.badRequest().body("No user with that email");
        String otp = otpService.generateAndSaveOtp(req.getEmail());
        emailService.sendSimpleMail(req.getEmail(), "Password Reset OTP", "Your password reset OTP: " + otp);
        return ResponseEntity.ok("OTP sent to email");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest req) {
        // 🔍 find email from OTP
        String email = otpService.findEmailByOtp(req.getOtp());
        if (email == null) {
            return ResponseEntity.badRequest().body("Invalid or expired OTP");
        }

        // ✅ update password
        userService.updatePassword(email, req.getNewPassword());

        // mark OTP as used automatically handled inside findEmailByOtp or can add manually
        return ResponseEntity.ok("Password updated successfully. You can now login.");
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<?> deleteAccount(@RequestBody DeleteAccountRequest req) {
        boolean deleted = userService.deleteUserByEmailOrMobile(req.getIdentifier());
        if (!deleted) {
            return ResponseEntity.badRequest().body("User not found");
        }
        return ResponseEntity.ok("Account deleted successfully.");
    }


}
