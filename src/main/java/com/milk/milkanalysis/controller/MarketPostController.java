package com.milk.milkanalysis.controller;

import com.milk.milkanalysis.model.MarketPost;
import com.milk.milkanalysis.model.PostCategory;
import com.milk.milkanalysis.service.MarketPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/market-post")
public class MarketPostController {

    @Autowired
    private MarketPostService service;

    // 🖼️ Serve uploaded images
    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) {
        try {
            Path imagePath = Paths.get("uploads/" + filename);

            if (!Files.exists(imagePath)) {
                return ResponseEntity.notFound().build();
            }

            byte[] imageBytes = Files.readAllBytes(imagePath);
            String contentType = Files.probeContentType(imagePath);
            if (contentType == null) {
                contentType = "image/jpeg";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(imageBytes);

        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 🟢 Add Post
    @PostMapping(value = "/add", consumes = {"multipart/form-data"})
    public ResponseEntity<MarketPost> addPost(
            @RequestPart("userName") String userName,
            @RequestPart("mobileNumber") String mobileNumber,
            @RequestPart("description") String description,
            @RequestPart("location") String location,
            @RequestPart(value = "category", required = false) String categoryStr,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        try {
            MarketPost post = new MarketPost();
            post.setUserName(userName);
            post.setMobileNumber(mobileNumber);
            post.setDescription(description);
            post.setLocation(location);
            post.setDate(LocalDate.now());

            // Category handling
            PostCategory category;
            try {
                category = PostCategory.valueOf(categoryStr.toUpperCase());
            } catch (Exception e) {
                category = PostCategory.OTHER;
            }
            post.setCategory(category);

            // 🖼️ FIXED: Save image with correct URL
            if (image != null && !image.isEmpty()) {
                String uploadDir = "uploads/";
                Files.createDirectories(Paths.get(uploadDir));

                String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();
                Path filePath = Paths.get(uploadDir + fileName);
                Files.write(filePath, image.getBytes());

                // Set correct URL for frontend access
                post.setImageUrl("/api/market-post/images/" + fileName);
                System.out.println("✅ Image saved: " + fileName);
            }

            MarketPost savedPost = service.addPost(post);
            return ResponseEntity.ok(savedPost);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    // Other methods remain same...
    // 🟢 Get All Posts
    @GetMapping("/all")
    public ResponseEntity<List<MarketPost>> getAllPosts() {
        return ResponseEntity.ok(service.getAllPosts());
    }

    // 👤 Get User Posts
    @GetMapping("/user/{mobileNumber}")
    public ResponseEntity<List<MarketPost>> getUserPosts(@PathVariable String mobileNumber) {
        return ResponseEntity.ok(service.getUserPosts(mobileNumber));
    }

    // 🟣 Get By Category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<MarketPost>> getByCategory(@PathVariable String category) {
        try {
            PostCategory cat = PostCategory.valueOf(category.toUpperCase());
            return ResponseEntity.ok(service.getByCategory(cat));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // ✏️ Update Post
    @PutMapping("/update/{id}/{mobileNumber}")
    public ResponseEntity<?> updatePost(@PathVariable String id,
                                        @PathVariable String mobileNumber,
                                        @RequestBody MarketPost updatedPost) {
        var result = service.updatePost(id, updatedPost, mobileNumber);
        if (result.isEmpty())
            return ResponseEntity.status(403).body("Access denied or post not found");
        return ResponseEntity.ok(result.get());
    }

    // 🔴 Delete Post
    @DeleteMapping("/delete/{id}/{mobileNumber}")
    public ResponseEntity<?> deletePost(@PathVariable String id,
                                        @PathVariable String mobileNumber) {
        boolean deleted = service.deletePost(id, mobileNumber);
        if (!deleted)
            return ResponseEntity.status(403).body("Access denied or post not found");
        return ResponseEntity.ok("Post deleted successfully");
    }
}
