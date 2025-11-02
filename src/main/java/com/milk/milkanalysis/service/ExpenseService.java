package com.milk.milkanalysis.service;

import com.milk.milkanalysis.dto.ExpenseRequest;
import com.milk.milkanalysis.model.Expense;
import com.milk.milkanalysis.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository repo;

    public Expense addExpense(ExpenseRequest req) {
        Expense expense = Expense.builder()
                .userId(req.getUserId())
                .feedCost(req.getFeedCost())
                .powderCost(req.getPowderCost())
                .labourCost(req.getLabourCost())
                .medicalCost(req.getMedicalCost())
                .machineryCost(req.getMachineryCost())
                .build();
        return repo.save(expense);
    }

    public List<Expense> getUserExpenses(String userId) {
        return repo.findByUserId(userId);
    }

    public Optional<Expense> updateExpense(String id, ExpenseRequest req, String userId) {
        Optional<Expense> opt = repo.findById(id);
        if (opt.isPresent() && opt.get().getUserId().equals(userId)) {
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

    public boolean deleteExpense(String id, String userId) {
        Optional<Expense> opt = repo.findById(id);
        if (opt.isPresent() && opt.get().getUserId().equals(userId)) {
            repo.delete(opt.get());
            return true;
        }
        return false;
    }
}
