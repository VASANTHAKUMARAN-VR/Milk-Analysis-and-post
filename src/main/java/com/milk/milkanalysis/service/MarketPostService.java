package com.milk.milkanalysis.service;

import com.milk.milkanalysis.model.MarketPost;
import com.milk.milkanalysis.model.PostCategory;
import com.milk.milkanalysis.repository.MarketPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MarketPostService {

    @Autowired
    private MarketPostRepository repo;

    // Create Post
    public MarketPost addPost(MarketPost post) {
        post.setDate(LocalDate.now());
        return repo.save(post);
    }

    // Get All Posts
    public List<MarketPost> getAllPosts() {
        return repo.findAll();
    }

    // Get Posts by User
    public List<MarketPost> getUserPosts(String mobileNumber) {
        return repo.findByMobileNumber(mobileNumber);
    }

    // Get Posts by Category
    public List<MarketPost> getByCategory(PostCategory category) {
        return repo.findByCategory(category);
    }

    // Update Post (only owner)
    public Optional<MarketPost> updatePost(String id, MarketPost updatedPost, String mobileNumber) {
        Optional<MarketPost> existingOpt = repo.findById(id);
        if (existingOpt.isPresent()) {
            MarketPost existing = existingOpt.get();
            if (existing.getMobileNumber().equals(mobileNumber)) {
                existing.setDescription(updatedPost.getDescription());
                existing.setLocation(updatedPost.getLocation());
                existing.setCategory(updatedPost.getCategory());
                existing.setImageUrl(updatedPost.getImageUrl());
                existing.setDate(LocalDate.now());
                return Optional.of(repo.save(existing));
            }
        }
        return Optional.empty();
    }

    // Delete Post (only owner)
    public boolean deletePost(String id, String mobileNumber) {
        Optional<MarketPost> existingOpt = repo.findById(id);
        if (existingOpt.isPresent() && existingOpt.get().getMobileNumber().equals(mobileNumber)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }
}
