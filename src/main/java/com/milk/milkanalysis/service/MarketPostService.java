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

    // ✅ Create a new market post
    public MarketPost addPost(MarketPost post) {
        post.setDate(LocalDate.now());
        return repo.save(post);
    }

    // ✅ Get all posts
    public List<MarketPost> getAllPosts() {
        return repo.findAll();
    }

    // ✅ Get all posts created by a specific user
    public List<MarketPost> getUserPosts(String mobileNumber) {
        return repo.findByMobileNumber(mobileNumber);
    }

    // ✅ Get posts by category
    public List<MarketPost> getByCategory(PostCategory category) {
        return repo.findByCategory(category);
    }

    // ✅ Update a post (only if the user is the owner)
    public Optional<MarketPost> updatePost(String id, MarketPost updatedPost, String mobileNumber) {
        Optional<MarketPost> opt = repo.findById(id);

        if (opt.isPresent() && opt.get().getMobileNumber().equals(mobileNumber)) {
            MarketPost existing = opt.get();
            existing.setDescription(updatedPost.getDescription());
            existing.setLocation(updatedPost.getLocation());
            existing.setCategory(updatedPost.getCategory());
            existing.setImageUrl(updatedPost.getImageUrl());
            existing.setDate(LocalDate.now());
            return Optional.of(repo.save(existing));
        }
        return Optional.empty();
    }

    // ✅ Delete post (only owner can delete)
    public boolean deletePost(String id, String mobileNumber) {
        Optional<MarketPost> opt = repo.findById(id);
        if (opt.isPresent() && opt.get().getMobileNumber().equals(mobileNumber)) {
            repo.delete(opt.get());
            return true;
        }
        return false;
    }
}
