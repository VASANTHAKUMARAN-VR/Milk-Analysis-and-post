package com.milk.milkanalysis.controller;

import com.milk.milkanalysis.model.SaleRecord;
import com.milk.milkanalysis.model.PostCategory;
import com.milk.milkanalysis.service.SaleRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sale-records")
public class SaleRecordController {

    @Autowired
    private SaleRecordService service;

    // ➕ Add Sale
    @PostMapping("/add")
    public ResponseEntity<SaleRecord> addSale(@RequestBody SaleRecord record) {
        return ResponseEntity.ok(service.addSale(record));
    }

    // 👤 Get Sales by User
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<SaleRecord>> getByUser(@PathVariable String userId) {
        return ResponseEntity.ok(service.getSalesByUser(userId));
    }

    // 🏷️ Get Sales by Category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<SaleRecord>> getByCategory(@PathVariable String category) {
        try {
            PostCategory cat = PostCategory.valueOf(category.toUpperCase());
            return ResponseEntity.ok(service.getSalesByCategory(cat));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 🗓️ Get Sales by Date
    @GetMapping("/date/{date}")
    public ResponseEntity<List<SaleRecord>> getByDate(@PathVariable String date) {
        return ResponseEntity.ok(service.getSalesByDate(LocalDate.parse(date)));
    }

    // ✏️ Update Sale
    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateSale(@PathVariable String id, @RequestBody SaleRecord updated) {
        var result = service.updateSale(id, updated);
        if (result.isEmpty())
            return ResponseEntity.status(404).body("Sale not found");
        return ResponseEntity.ok(result.get());
    }

    // ❌ Delete Sale
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteSale(@PathVariable String id) {
        boolean deleted = service.deleteSale(id);
        if (!deleted)
            return ResponseEntity.status(404).body("Sale not found");
        return ResponseEntity.ok("Sale deleted successfully");
    }
}
