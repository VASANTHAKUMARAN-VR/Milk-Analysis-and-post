package com.milk.milkanalysis.service;

import com.milk.milkanalysis.dto.CowSaleRequest;
import com.milk.milkanalysis.model.CowSale;
import com.milk.milkanalysis.repository.CowSaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CowSaleService {

    @Autowired
    private CowSaleRepository repo;

    // Add new sale
    public CowSale addSale(CowSaleRequest req) {
        CowSale sale = CowSale.builder()
                .userId(req.getUserId())
                .buyerName(req.getBuyerName())
                .quantity(req.getQuantity())
                .totalAmount(req.getTotalAmount())
                .date(LocalDate.parse(req.getDate()))
                .build();
        return repo.save(sale);
    }

    // Get all sales by user
    public List<CowSale> getUserSales(String userId) {
        return repo.findByUserId(userId);
    }

    // Update sale (only owner)
    public Optional<CowSale> updateSale(String id, CowSaleRequest req, String userId) {
        Optional<CowSale> opt = repo.findById(id);
        if (opt.isPresent() && opt.get().getUserId().equals(userId)) {
            CowSale existing = opt.get();
            existing.setBuyerName(req.getBuyerName());
            existing.setQuantity(req.getQuantity());
            existing.setTotalAmount(req.getTotalAmount());
            existing.setDate(LocalDate.parse(req.getDate()));
            return Optional.of(repo.save(existing));
        }
        return Optional.empty();
    }

    // Delete sale (only owner)
    public boolean deleteSale(String id, String userId) {
        Optional<CowSale> opt = repo.findById(id);
        if (opt.isPresent() && opt.get().getUserId().equals(userId)) {
            repo.delete(opt.get());
            return true;
        }
        return false;
    }
}
