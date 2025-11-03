package com.milk.milkanalysis.service;

import com.milk.milkanalysis.model.CowSalePost;
import com.milk.milkanalysis.repository.CowSalePostRepository;
import com.milk.milkanalysis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CowSalePostService {

    @Autowired
    private CowSalePostRepository repo;

    @Autowired
    private UserRepository userRepository;

    // 🧩 Create new cow sale post
    public CowSalePost addPost(CowSalePost post) {
        // Find user using either email or mobile
        var userOpt = userRepository.findByEmailOrMobileNumber(post.getMobileNumber(), post.getMobileNumber());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found for: " + post.getMobileNumber());
        }

        // Always use mobile number as the unique user ID ✅
        String actualUserId = userOpt.get().getMobileNumber();

        post.setMobileNumber(actualUserId);
        post.setDate(LocalDate.now());
        return repo.save(post);
    }

    // 🧩 Get all sale posts
    public List<CowSalePost> getAllPosts() {
        return repo.findAll();
    }

    // 🧩 Get user’s own sale posts (email or mobile login)
    public List<CowSalePost> getUserPosts(String identifier) {
        var userOpt = userRepository.findByEmailOrMobileNumber(identifier, identifier);
        if (userOpt.isEmpty()) {
            return List.of();
        }

        // Always use mobile number as the unique user ID ✅
        String actualUserId = userOpt.get().getMobileNumber();
        return repo.findByMobileNumber(actualUserId);
    }

    // 🧩 Update sale post (only if owned by same user)
    public Optional<CowSalePost> updatePost(String id, CowSalePost updatedPost, String identifier) {
        Optional<CowSalePost> existingOpt = repo.findById(id);
        if (existingOpt.isPresent()) {
            var userOpt = userRepository.findByEmailOrMobileNumber(identifier, identifier);
            if (userOpt.isEmpty()) {
                return Optional.empty();
            }

            // Always use mobile number as the unique user ID ✅
            String actualUserId = userOpt.get().getMobileNumber();
            CowSalePost existing = existingOpt.get();

            if (existing.getMobileNumber().equals(actualUserId)) {
                existing.setDescription(updatedPost.getDescription());
                existing.setLocation(updatedPost.getLocation());
                existing.setImageUrl(updatedPost.getImageUrl());
                existing.setDate(LocalDate.now());
                return Optional.of(repo.save(existing));
            }
        }
        return Optional.empty();
    }

    // 🧩 Delete sale post (only if owned by same user)
    public boolean deletePost(String id, String identifier) {
        Optional<CowSalePost> existingOpt = repo.findById(id);
        if (existingOpt.isPresent()) {
            var userOpt = userRepository.findByEmailOrMobileNumber(identifier, identifier);
            if (userOpt.isEmpty()) {
                return false;
            }

            // Always use mobile number as the unique user ID ✅
            String actualUserId = userOpt.get().getMobileNumber();

            if (existingOpt.get().getMobileNumber().equals(actualUserId)) {
                repo.deleteById(id);
                return true;
            }
        }
        return false;
    }
}
