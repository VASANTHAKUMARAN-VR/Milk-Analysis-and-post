package com.milk.milkanalysis.service;

import com.milk.milkanalysis.dto.CowSaleRequest;
import com.milk.milkanalysis.model.CowSale;
import com.milk.milkanalysis.repository.CowSaleRepository;
import com.milk.milkanalysis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CowSaleService {

    @Autowired
    private CowSaleRepository repo;

    @Autowired
    private UserRepository userRepository;

    // 🧩 Add new cow sale
    public CowSale addSale(CowSaleRequest req) {
        // Always map to user's mobile number (universal ID)
        var userOpt = userRepository.findByEmailOrMobileNumber(req.getUserId(), req.getUserId());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found for identifier: " + req.getUserId());
        }

        String actualUserId = userOpt.get().getMobileNumber();

        CowSale sale = CowSale.builder()
                .userId(actualUserId)
                .buyerName(req.getBuyerName())
                .quantity(req.getQuantity())
                .totalAmount(req.getTotalAmount())
                .date(LocalDate.parse(req.getDate()))
                .build();

        return repo.save(sale);
    }

    // 🧩 Get all sales for a specific user (email or mobile)
    public List<CowSale> getUserSales(String identifier) {
        var userOpt = userRepository.findByEmailOrMobileNumber(identifier, identifier);
        if (userOpt.isEmpty()) {
            return List.of(); // no user found
        }

        String actualUserId = userOpt.get().getMobileNumber();
        return repo.findByUserId(actualUserId);
    }

    // 🧩 Update sale (only if owned by the same user)
    public Optional<CowSale> updateSale(String id, CowSaleRequest req, String identifier) {
        var userOpt = userRepository.findByEmailOrMobileNumber(identifier, identifier);
        if (userOpt.isEmpty()) return Optional.empty();

        String actualUserId = userOpt.get().getMobileNumber();

        Optional<CowSale> opt = repo.findById(id);
        if (opt.isPresent() && opt.get().getUserId().equals(actualUserId)) {
            CowSale existing = opt.get();
            existing.setBuyerName(req.getBuyerName());
            existing.setQuantity(req.getQuantity());
            existing.setTotalAmount(req.getTotalAmount());
            existing.setDate(LocalDate.parse(req.getDate()));
            return Optional.of(repo.save(existing));
        }
        return Optional.empty();
    }

    // 🧩 Delete sale (only if owned by the same user)
    public boolean deleteSale(String id, String identifier) {
        var userOpt = userRepository.findByEmailOrMobileNumber(identifier, identifier);
        if (userOpt.isEmpty()) return false;

        String actualUserId = userOpt.get().getMobileNumber();

        Optional<CowSale> opt = repo.findById(id);
        if (opt.isPresent() && opt.get().getUserId().equals(actualUserId)) {
            repo.delete(opt.get());
            return true;
        }
        return false;
    }
}
