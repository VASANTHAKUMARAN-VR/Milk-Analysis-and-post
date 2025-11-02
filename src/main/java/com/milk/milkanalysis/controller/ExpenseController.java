package com.milk.milkanalysis.controller;

import com.milk.milkanalysis.dto.ExpenseRequest;
import com.milk.milkanalysis.model.Expense;
import com.milk.milkanalysis.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping("/add")
    public ResponseEntity<?> addExpense(@RequestBody ExpenseRequest req) {
        return ResponseEntity.ok(expenseService.addExpense(req));
    }

    @GetMapping("/myexpenses/{userId}")
    public ResponseEntity<List<Expense>> getMyExpenses(@PathVariable String userId) {
        return ResponseEntity.ok(expenseService.getUserExpenses(userId));
    }

    @PutMapping("/update/{id}/{userId}")
    public ResponseEntity<?> updateExpense(@PathVariable String id,
                                           @PathVariable String userId,
                                           @RequestBody ExpenseRequest req) {
        var updated = expenseService.updateExpense(id, req, userId);
        if (updated.isEmpty()) return ResponseEntity.status(403).body("Access denied or record not found");
        return ResponseEntity.ok(updated.get());
    }

    @DeleteMapping("/delete/{id}/{userId}")
    public ResponseEntity<?> deleteExpense(@PathVariable String id, @PathVariable String userId) {
        boolean deleted = expenseService.deleteExpense(id, userId);
        if (!deleted) return ResponseEntity.status(403).body("Access denied or record not found");
        return ResponseEntity.ok("Record deleted successfully");
    }
}
