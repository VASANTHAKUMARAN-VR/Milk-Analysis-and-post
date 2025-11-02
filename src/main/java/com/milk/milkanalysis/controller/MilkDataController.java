package com.milk.milkanalysis.controller;

import com.milk.milkanalysis.dto.MilkDataRequest;
import com.milk.milkanalysis.model.MilkData;
import com.milk.milkanalysis.service.MilkDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/milk")
public class MilkDataController {

    @Autowired
    private MilkDataService milkDataService;

    // 🧩 Add new milk data (auto-calculates amount)
    @PostMapping("/add")
    public ResponseEntity<?> addMilkData(@RequestBody MilkDataRequest req) {
        MilkData saved = milkDataService.saveMilkData(req);
        return ResponseEntity.ok(Map.of(
                "message", "Milk data added successfully",
                "data", saved
        ));
    }

    // 🧩 Get all milk data for a specific user
    @GetMapping("/mydata/{userId}")
    public ResponseEntity<?> getMyData(@PathVariable String userId) {
        List<MilkData> dataList = milkDataService.getUserMilkData(userId);
        if (dataList.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "No milk data found for this user"));
        }
        return ResponseEntity.ok(dataList);
    }

    // 🧩 Update a record (only if it belongs to the same user)
    @PutMapping("/update/{id}/{userId}")
    public ResponseEntity<?> updateMilkData(@PathVariable String id,
                                            @PathVariable String userId,
                                            @RequestBody MilkDataRequest req) {
        var updated = milkDataService.updateMilkData(id, req, userId);
        if (updated.isEmpty()) {
            return ResponseEntity.status(403).body(Map.of(
                    "error", "Access denied or record not found"
            ));
        }
        return ResponseEntity.ok(Map.of(
                "message", "Milk data updated successfully",
                "data", updated.get()
        ));
    }

    // 🧩 Delete a record (only if it belongs to the same user)
    @DeleteMapping("/delete/{id}/{userId}")
    public ResponseEntity<?> deleteMilkData(@PathVariable String id, @PathVariable String userId) {
        boolean deleted = milkDataService.deleteMilkData(id, userId);
        if (!deleted) {
            return ResponseEntity.status(403).body(Map.of(
                    "error", "Access denied or record not found"
            ));
        }
        return ResponseEntity.ok(Map.of(
                "message", "Record deleted successfully"
        ));
    }
}
