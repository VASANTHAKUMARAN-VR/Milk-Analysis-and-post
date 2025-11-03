package com.milk.milkanalysis.controller;

import com.milk.milkanalysis.model.BuyRecord;
import com.milk.milkanalysis.service.BuyRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/buy")
public class BuyRecordController {

    @Autowired
    private BuyRecordService service;

    // ➕ Add Buy
    @PostMapping("/add")
    public ResponseEntity<BuyRecord> addBuy(@RequestBody BuyRecord record) {
        return ResponseEntity.ok(service.addBuy(record));
    }

    // 👤 Get all buys for a user
    @GetMapping("/mydata/{userId}")
    public ResponseEntity<List<BuyRecord>> getBuys(@PathVariable String userId) {
        return ResponseEntity.ok(service.getUserBuys(userId));
    }

    // ✏️ Update Buy - PATCH → PUT
    @PutMapping("/update/{id}/{userId}") // Changed from @PatchMapping
    public ResponseEntity<?> updateBuy(@PathVariable String id,
                                       @PathVariable String userId,
                                       @RequestBody BuyRecord updated) {
        var result = service.updateBuy(id, userId, updated);
        if (result.isEmpty())
            return ResponseEntity.status(403).body("Access denied or record not found");
        return ResponseEntity.ok(result.get());
    }

    // ❌ Delete Buy
    @DeleteMapping("/delete/{id}/{userId}")
    public ResponseEntity<?> deleteBuy(@PathVariable String id,
                                       @PathVariable String userId) {
        boolean deleted = service.deleteBuy(id, userId);
        if (!deleted)
            return ResponseEntity.status(403).body("Access denied or record not found");
        return ResponseEntity.ok("Buy record deleted successfully");
    }
}