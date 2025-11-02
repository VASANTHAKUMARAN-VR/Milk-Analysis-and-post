package com.milk.milkanalysis.controller;

import com.milk.milkanalysis.dto.CowSaleRequest;
import com.milk.milkanalysis.model.CowSale;
import com.milk.milkanalysis.service.CowSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cow-sale")
public class CowSaleController {

    @Autowired
    private CowSaleService service;

    // Add sale
    @PostMapping("/add")
    public ResponseEntity<CowSale> addSale(@RequestBody CowSaleRequest req) {
        return ResponseEntity.ok(service.addSale(req));
    }

    // Get all sales by user
    @GetMapping("/mydata/{userId}")
    public ResponseEntity<List<CowSale>> getSales(@PathVariable String userId) {
        return ResponseEntity.ok(service.getUserSales(userId));
    }

    // ✅ FIXED: Use PUT instead of PATCH
    @PutMapping("/update/{id}/{userId}")
    public ResponseEntity<?> updateSale(@PathVariable String id,
                                        @PathVariable String userId,
                                        @RequestBody CowSaleRequest req) {
        System.out.println("Cow Sale Update request received - ID: " + id + ", User: " + userId);
        System.out.println("Request data: " + req);

        var updated = service.updateSale(id, req, userId);
        if (updated.isEmpty()) {
            System.out.println("Cow Sale Update failed - Access denied or record not found");
            return ResponseEntity.status(403).body("Access denied or record not found");
        }

        System.out.println("Cow Sale Update successful: " + updated.get());
        return ResponseEntity.ok(updated.get());
    }

    // Delete sale
    @DeleteMapping("/delete/{id}/{userId}")
    public ResponseEntity<?> deleteSale(@PathVariable String id,
                                        @PathVariable String userId) {
        boolean deleted = service.deleteSale(id, userId);
        if (!deleted)
            return ResponseEntity.status(403).body("Access denied or record not found");
        return ResponseEntity.ok("Sale deleted successfully");
    }
}