package com.milk.milkanalysis.service;

import com.milk.milkanalysis.model.CowSalePost;
import com.milk.milkanalysis.repository.CowSalePostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CowSalePostService {

    @Autowired
    private CowSalePostRepository repo;

    // Create new cow sale post
    public CowSalePost addPost(CowSalePost post) {
        post.setDate(LocalDate.now());
        return repo.save(post);
    }

    // Get all sale posts
    public List<CowSalePost> getAllPosts() {
        return repo.findAll();
    }

    // Get user’s own sale posts
    public List<CowSalePost> getUserPosts(String mobileNumber) {
        return repo.findByMobileNumber(mobileNumber);
    }

    // Update sale post (only owner)
    public Optional<CowSalePost> updatePost(String id, CowSalePost updatedPost, String mobileNumber) {
        Optional<CowSalePost> existingOpt = repo.findById(id);
        if (existingOpt.isPresent()) {
            CowSalePost existing = existingOpt.get();
            if (existing.getMobileNumber().equals(mobileNumber)) {
                existing.setDescription(updatedPost.getDescription());
                existing.setLocation(updatedPost.getLocation());
                existing.setImageUrl(updatedPost.getImageUrl());
                existing.setDate(LocalDate.now());
                return Optional.of(repo.save(existing));
            }
        }
        return Optional.empty();
    }

    // Delete sale post (only owner)
    public boolean deletePost(String id, String mobileNumber) {
        Optional<CowSalePost> existingOpt = repo.findById(id);
        if (existingOpt.isPresent() && existingOpt.get().getMobileNumber().equals(mobileNumber)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }
}
