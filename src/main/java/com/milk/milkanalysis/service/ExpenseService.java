package com.milk.milkanalysis.service;

import com.milk.milkanalysis.dto.ExpenseRequest;
import com.milk.milkanalysis.model.Expense;
import com.milk.milkanalysis.repository.ExpenseRepository;
import com.milk.milkanalysis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository repo;

    @Autowired
    private UserRepository userRepository;

    // 🧩 Add new expense
    public Expense addExpense(ExpenseRequest req) {
        // Always use mobile number as real user ID
        var userOpt = userRepository.findByEmailOrMobileNumber(req.getUserId(), req.getUserId());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found for identifier: " + req.getUserId());
        }

        String actualUserId = userOpt.get().getMobileNumber();

        Expense expense = Expense.builder()
                .userId(actualUserId)
                .feedCost(req.getFeedCost())
                .powderCost(req.getPowderCost())
                .labourCost(req.getLabourCost())
                .medicalCost(req.getMedicalCost())
                .machineryCost(req.getMachineryCost())
                .build();

        return repo.save(expense);
    }

    // 🧩 Get all expenses for a user (works with email or mobile)
    public List<Expense> getUserExpenses(String identifier) {
        var userOpt = userRepository.findByEmailOrMobileNumber(identifier, identifier);
        if (userOpt.isEmpty()) {
            return List.of(); // no user found
        }

        String actualUserId = userOpt.get().getMobileNumber();
        return repo.findByUserId(actualUserId);
    }

    // 🧩 Update expense only if owned by the same user
    public Optional<Expense> updateExpense(String id, ExpenseRequest req, String identifier) {
        var userOpt = userRepository.findByEmailOrMobileNumber(identifier, identifier);
        if (userOpt.isEmpty()) return Optional.empty();

        String actualUserId = userOpt.get().getMobileNumber();

        Optional<Expense> opt = repo.findById(id);
        if (opt.isPresent() && opt.get().getUserId().equals(actualUserId)) {
            Expense existing = opt.get();
            existing.setFeedCost(req.getFeedCost());
            existing.setPowderCost(req.getPowderCost());
            existing.setLabourCost(req.getLabourCost());
            existing.setMedicalCost(req.getMedicalCost());
            existing.setMachineryCost(req.getMachineryCost());
            return Optional.of(repo.save(existing));
        }
        return Optional.empty();
    }

    // 🧩 Delete expense only if owned by the same user
    public boolean deleteExpense(String id, String identifier) {
        var userOpt = userRepository.findByEmailOrMobileNumber(identifier, identifier);
        if (userOpt.isEmpty()) return false;

        String actualUserId = userOpt.get().getMobileNumber();

        Optional<Expense> opt = repo.findById(id);
        if (opt.isPresent() && opt.get().getUserId().equals(actualUserId)) {
            repo.delete(opt.get());
            return true;
        }
        return false;
    }
}
