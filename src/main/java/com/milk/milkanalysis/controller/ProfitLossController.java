package com.milk.milkanalysis.controller;

import com.milk.milkanalysis.service.ProfitLossService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/profit")
public class ProfitLossController {

    @Autowired
    private ProfitLossService service;

    // 💰 Get Profit/Loss Summary
    @GetMapping("/{userId}")
    public ResponseEntity<Map<String, Object>> getProfitLoss(@PathVariable String userId) {
        double profitOrLoss = service.calculateProfit(userId);

        Map<String, Object> response = new HashMap<>();

        if (profitOrLoss > 0) {
            response.put("status", "Profit 💸");
            response.put("amount", profitOrLoss);
            response.put("message", "You have earned a profit of ₹" + profitOrLoss);
        } else if (profitOrLoss < 0) {
            response.put("status", "Loss 📉");
            response.put("amount", Math.abs(profitOrLoss));
            response.put("message", "You have incurred a loss of ₹" + Math.abs(profitOrLoss));
        } else {
            response.put("status", "Break Even ⚖️");
            response.put("amount", 0);
            response.put("message", "No profit, no loss. Balanced accounts!");
        }

        return ResponseEntity.ok(response);
    }
}
