package com.milk.milkanalysis.service;

import com.milk.milkanalysis.dto.CowPurchaseRequest;
import com.milk.milkanalysis.model.CowPurchase;
import com.milk.milkanalysis.repository.CowPurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CowPurchaseService {

    @Autowired
    private CowPurchaseRepository repo;

    // Add new purchase
    public CowPurchase addCowPurchase(CowPurchaseRequest req) {
        CowPurchase purchase = CowPurchase.builder()
                .userId(req.getUserId())
                .salesmanName(req.getSalesmanName())
                .quantity(req.getQuantity())
                .totalAmount(req.getTotalAmount())
                .purchasePlace(req.getPurchasePlace())
                .date(LocalDate.parse(req.getDate()))
                .build();
        return repo.save(purchase);
    }

    // Get all purchases for a specific user
    public List<CowPurchase> getUserPurchases(String userId) {
        return repo.findByUserId(userId);
    }

    // ✅ FIXED: Update method (PUT instead of PATCH for simplicity)
    public Optional<CowPurchase> updatePurchase(String id, CowPurchaseRequest req, String userId) {
        Optional<CowPurchase> opt = repo.findById(id);
        if (opt.isPresent() && opt.get().getUserId().equals(userId)) {
            CowPurchase existing = opt.get();

            // Update all fields
            existing.setSalesmanName(req.getSalesmanName());
            existing.setQuantity(req.getQuantity());
            existing.setTotalAmount(req.getTotalAmount());
            existing.setPurchasePlace(req.getPurchasePlace());
            existing.setDate(LocalDate.parse(req.getDate()));

            return Optional.of(repo.save(existing));
        }
        return Optional.empty();
    }

    // Delete purchase (only owner can delete)
    public boolean deletePurchase(String id, String userId) {
        Optional<CowPurchase> opt = repo.findById(id);
        if (opt.isPresent() && opt.get().getUserId().equals(userId)) {
            repo.delete(opt.get());
            return true;
        }
        return false;
    }
}