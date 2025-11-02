package com.milk.milkanalysis.controller;

import com.milk.milkanalysis.model.CowSalePost;
import com.milk.milkanalysis.service.CowSalePostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/cow-sale-post")
public class CowSalePostController {

    @Autowired
    private CowSalePostService service;

    // 🖼️ Serve uploaded images - ADD THIS METHOD
    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
        try {
            Path imagePath = Paths.get("uploads/" + filename);

            // Check if file exists
            if (!Files.exists(imagePath)) {
                System.out.println("❌ Image not found: " + imagePath);
                return ResponseEntity.notFound().build();
            }

            byte[] imageBytes = Files.readAllBytes(imagePath);

            // Determine content type
            String contentType = Files.probeContentType(imagePath);
            if (contentType == null) {
                contentType = "image/jpeg"; // default fallback
            }

            System.out.println("✅ Serving image: " + filename + " | Type: " + contentType);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(imageBytes);

        } catch (IOException e) {
            System.out.println("❌ Error serving image: " + filename + " | Error: " + e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(value = "/add", consumes = {"multipart/form-data"})
    public ResponseEntity<CowSalePost> addCowSalePost(
            @RequestPart("userName") String userName,
            @RequestPart("mobileNumber") String mobileNumber,
            @RequestPart("description") String description,
            @RequestPart("location") String location,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        try {
            CowSalePost post = new CowSalePost();
            post.setUserName(userName);
            post.setMobileNumber(mobileNumber);
            post.setDescription(description);
            post.setLocation(location);
            post.setDate(LocalDate.now());

            // 🖼️ FIXED IMAGE UPLOAD
            if (image != null && !image.isEmpty()) {
                // Create uploads directory in project root
                String uploadDir = "uploads/";
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // Generate unique filename
                String fileName = System.currentTimeMillis() + "_" +
                        image.getOriginalFilename().replace(" ", "_");
                Path filePath = uploadPath.resolve(fileName);

                // Save file
                Files.write(filePath, image.getBytes());

                // Set URL that frontend can access
                post.setImageUrl("/api/cow-sale-post/images/" + fileName); // 👈 FIXED URL PATH
                System.out.println("✅ Image saved: " + filePath.toString());
                System.out.println("🔗 Image URL: " + post.getImageUrl());
                System.out.println("📸 Image size: " + image.getSize() + " bytes");
                System.out.println("📸 Image type: " + image.getContentType());
            } else {
                post.setImageUrl(null);
                System.out.println("ℹ️ No image uploaded");
            }

            CowSalePost saved = service.addPost(post);
            System.out.println("✅ Post created successfully with ID: " + saved.getId());
            return ResponseEntity.ok(saved);

        } catch (IOException e) {
            System.out.println("❌ Error in addCowSalePost: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // 🟢 GET all posts
    @GetMapping("/all")
    public ResponseEntity<List<CowSalePost>> getAllPosts() {
        List<CowSalePost> posts = service.getAllPosts();
        System.out.println("📋 Returning " + posts.size() + " posts");
        posts.forEach(post -> {
            System.out.println("   - Post ID: " + post.getId() + " | Image: " + post.getImageUrl());
        });
        return ResponseEntity.ok(posts);
    }

    // 🟢 GET user's own posts
    @GetMapping("/my/{mobileNumber}")
    public ResponseEntity<List<CowSalePost>> getUserPosts(@PathVariable String mobileNumber) {
        List<CowSalePost> posts = service.getUserPosts(mobileNumber);
        System.out.println("📋 Returning " + posts.size() + " posts for user: " + mobileNumber);
        return ResponseEntity.ok(posts);
    }

    // 🟡 UPDATE post
    @PutMapping("/update/{id}/{mobileNumber}")
    public ResponseEntity<?> updatePost(@PathVariable String id,
                                        @PathVariable String mobileNumber,
                                        @RequestBody CowSalePost updatedPost) {
        System.out.println("🔄 Cow Sale Post Update request - ID: " + id + ", User: " + mobileNumber);
        System.out.println("📝 Update data - Desc: " + updatedPost.getDescription() +
                " | Location: " + updatedPost.getLocation() +
                " | Image: " + updatedPost.getImageUrl());

        var result = service.updatePost(id, updatedPost, mobileNumber);
        if (result.isEmpty()) {
            System.out.println("❌ Update failed - Access denied or post not found");
            return ResponseEntity.status(403).body("Access denied or post not found");
        }

        System.out.println("✅ Update successful: " + result.get().getId());
        return ResponseEntity.ok(result.get());
    }

    // 🔴 DELETE post
    @DeleteMapping("/delete/{id}/{mobileNumber}")
    public ResponseEntity<?> deletePost(@PathVariable String id,
                                        @PathVariable String mobileNumber) {
        System.out.println("🗑️ Delete request - ID: " + id + ", User: " + mobileNumber);
        boolean deleted = service.deletePost(id, mobileNumber);
        if (!deleted) {
            System.out.println("❌ Delete failed - Access denied or post not found");
            return ResponseEntity.status(403).body("Access denied or post not found");
        }
        System.out.println("✅ Delete successful");
        return ResponseEntity.ok("Post deleted successfully");
    }
}