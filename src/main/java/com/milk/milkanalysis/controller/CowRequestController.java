package com.milk.milkanalysis.controller;

import com.milk.milkanalysis.model.CowRequest;
import com.milk.milkanalysis.service.CowRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cowrequests")
@CrossOrigin(origins = "*")
public class CowRequestController {

    @Autowired
    private CowRequestService service;

    // Create new request
    @PostMapping
    public ResponseEntity<CowRequest> createRequest(@RequestBody CowRequest req) {
        return ResponseEntity.ok(service.createRequest(req));
    }

    // Get all requests (visible to everyone)
    @GetMapping
    public ResponseEntity<List<CowRequest>> getAllRequests() {
        return ResponseEntity.ok(service.getAllRequests());
    }

    // Get user’s own requests
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CowRequest>> getUserRequests(@PathVariable String userId) {
        return ResponseEntity.ok(service.getRequestsByUser(userId));
    }

    // Update request — only if owner
    @PatchMapping("/{id}/{userId}")
    public ResponseEntity<?> updateRequest(@PathVariable String id,
                                           @PathVariable String userId,
                                           @RequestBody CowRequest updated) {
        return service.updateRequest(id, userId, updated)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(403).body("Access Denied or Record Not Found"));
    }

    // Delete request — only if owner
    @DeleteMapping("/{id}/{userId}")
    public ResponseEntity<?> deleteRequest(@PathVariable String id,
                                           @PathVariable String userId) {
        boolean deleted = service.deleteRequest(id, userId);
        if (deleted) return ResponseEntity.ok("Request deleted successfully");
        return ResponseEntity.status(403).body("Access Denied or Record Not Found");
    }
}
