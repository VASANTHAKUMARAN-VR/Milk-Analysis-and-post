package com.milk.milkanalysis.service;

import com.milk.milkanalysis.dto.CowPurchaseRequest;
import com.milk.milkanalysis.model.CowPurchase;
import com.milk.milkanalysis.repository.CowPurchaseRepository;
import com.milk.milkanalysis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CowPurchaseService {

    @Autowired
    private CowPurchaseRepository repo;

    @Autowired
    private UserRepository userRepository;

    // 🧩 Add new cow purchase
    public CowPurchase addCowPurchase(CowPurchaseRequest req) {
        // Always map to mobile number
        var userOpt = userRepository.findByEmailOrMobileNumber(req.getUserId(), req.getUserId());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found for identifier: " + req.getUserId());
        }

        String actualUserId = userOpt.get().getMobileNumber();

        CowPurchase purchase = CowPurchase.builder()
                .userId(actualUserId)
                .salesmanName(req.getSalesmanName())
                .quantity(req.getQuantity())
                .totalAmount(req.getTotalAmount())
                .purchasePlace(req.getPurchasePlace())
                .date(LocalDate.parse(req.getDate()))
                .build();

        return repo.save(purchase);
    }

    // 🧩 Get all purchases for the same user (works with email or mobile)
    public List<CowPurchase> getUserPurchases(String identifier) {
        var userOpt = userRepository.findByEmailOrMobileNumber(identifier, identifier);
        if (userOpt.isEmpty()) {
            return List.of(); // No user found
        }

        String actualUserId = userOpt.get().getMobileNumber();
        return repo.findByUserId(actualUserId);
    }

    // 🧩 Update purchase (only if user owns it)
    public Optional<CowPurchase> updatePurchase(String id, CowPurchaseRequest req, String identifier) {
        var userOpt = userRepository.findByEmailOrMobileNumber(identifier, identifier);
        if (userOpt.isEmpty()) return Optional.empty();

        String actualUserId = userOpt.get().getMobileNumber();

        Optional<CowPurchase> opt = repo.findById(id);
        if (opt.isPresent() && opt.get().getUserId().equals(actualUserId)) {
            CowPurchase existing = opt.get();
            existing.setSalesmanName(req.getSalesmanName());
            existing.setQuantity(req.getQuantity());
            existing.setTotalAmount(req.getTotalAmount());
            existing.setPurchasePlace(req.getPurchasePlace());
            existing.setDate(LocalDate.parse(req.getDate()));
            return Optional.of(repo.save(existing));
        }
        return Optional.empty();
    }

    // 🧩 Delete purchase (only if user owns it)
    public boolean deletePurchase(String id, String identifier) {
        var userOpt = userRepository.findByEmailOrMobileNumber(identifier, identifier);
        if (userOpt.isEmpty()) return false;

        String actualUserId = userOpt.get().getMobileNumber();

        Optional<CowPurchase> opt = repo.findById(id);
        if (opt.isPresent() && opt.get().getUserId().equals(actualUserId)) {
            repo.delete(opt.get());
            return true;
        }
        return false;
    }
}
