package com.milk.milkanalysis.controller;

import com.milk.milkanalysis.dto.CowPurchaseRequest;
import com.milk.milkanalysis.model.CowPurchase;
import com.milk.milkanalysis.service.CowPurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cow-purchase")
public class CowPurchaseController {

    @Autowired
    private CowPurchaseService service;

    // Add new cow purchase
    @PostMapping("/add")
    public ResponseEntity<CowPurchase> addPurchase(@RequestBody CowPurchaseRequest req) {
        return ResponseEntity.ok(service.addCowPurchase(req));
    }

    // Get all purchases by user
    @GetMapping("/mydata/{userId}")
    public ResponseEntity<List<CowPurchase>> getUserPurchases(@PathVariable String userId) {
        return ResponseEntity.ok(service.getUserPurchases(userId));
    }

    // ✅ FIXED: Use PUT instead of PATCH
    @PutMapping("/update/{id}/{userId}")
    public ResponseEntity<?> updatePurchase(@PathVariable String id,
                                            @PathVariable String userId,
                                            @RequestBody CowPurchaseRequest req) {
        System.out.println("Update request received - ID: " + id + ", User: " + userId);
        System.out.println("Request data: " + req);

        Optional<CowPurchase> updated = service.updatePurchase(id, req, userId);
        if (updated.isEmpty()) {
            System.out.println("Update failed - Access denied or record not found");
            return ResponseEntity.status(403).body("Access denied or record not found");
        }

        System.out.println("Update successful: " + updated.get());
        return ResponseEntity.ok(updated.get());
    }

    // Delete purchase
    @DeleteMapping("/delete/{id}/{userId}")
    public ResponseEntity<?> deletePurchase(@PathVariable String id,
                                            @PathVariable String userId) {
        boolean deleted = service.deletePurchase(id, userId);
        if (!deleted)
            return ResponseEntity.status(403).body("Access denied or record not found");
        return ResponseEntity.ok("Purchase deleted successfully");
    }
}