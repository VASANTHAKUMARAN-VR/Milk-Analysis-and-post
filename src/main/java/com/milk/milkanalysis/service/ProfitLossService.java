package com.milk.milkanalysis.service;

import com.milk.milkanalysis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfitLossService {

    @Autowired
    private CowSaleRepository cowSaleRepo;

    @Autowired
    private CowPurchaseRepository cowPurchaseRepo;

    @Autowired
    private SaleRecordRepository saleRecordRepo;

    @Autowired
    private BuyRecordRepository buyRecordRepo;

    @Autowired
    private ExpenseRepository expenseRepo;

    @Autowired
    private MilkDataRepository milkDataRepo; // 👈 Add this line

    public double calculateProfit(String userId) {
        // 🐄 Cow Sales
        double totalCowSales = cowSaleRepo.findByUserId(userId)
                .stream().mapToDouble(s -> s.getTotalAmount()).sum();

        // 🥛 Product Sales (Milk, Grass, etc.)
        double totalMilkSales = saleRecordRepo.findByUserId(userId)
                .stream().mapToDouble(s -> s.getPrice()).sum();

        // 💧 Milk Data income (liters × rate)
        double totalMilkIncome = milkDataRepo.findByUserId(userId)
                .stream().mapToDouble(m -> m.getAmount()).sum(); // 👈 Milk added here

        // 🐮 Cow Purchases
        double totalCowPurchases = cowPurchaseRepo.findByUserId(userId)
                .stream().mapToDouble(p -> p.getTotalAmount()).sum();

        // 🛒 Item Purchases (Buy Records)
        double totalBuyRecords = buyRecordRepo.findByUserId(userId)
                .stream().mapToDouble(b -> b.getPrice()).sum();

        // 💰 Expenses (sum of all expense categories)
        double totalExpenses = expenseRepo.findByUserId(userId)
                .stream()
                .mapToDouble(e -> e.getFeedCost()
                        + e.getPowderCost()
                        + e.getLabourCost()
                        + e.getMedicalCost()
                        + e.getMachineryCost())
                .sum();

        // 📊 Calculate totals
        double income = totalCowSales + totalMilkSales + totalMilkIncome; // 👈 Added milk income
        double expenses = totalCowPurchases + totalBuyRecords + totalExpenses;

        return income - expenses;
    }
}
