package com.milk.milkanalysis.service;

import com.milk.milkanalysis.model.BuyRecord;
import com.milk.milkanalysis.repository.BuyRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BuyRecordService {

    @Autowired
    private BuyRecordRepository repo;

    // ➕ Add record
    public BuyRecord addBuy(BuyRecord record) {
        record.setDate(LocalDate.now());
        return repo.save(record);
    }

    // 👤 Get all records by user
    public List<BuyRecord> getUserBuys(String userId) {
        return repo.findByUserId(userId);
    }

    // ✏️ Update record
    public Optional<BuyRecord> updateBuy(String id, String userId, BuyRecord updated) {
        Optional<BuyRecord> existing = repo.findById(id);
        if (existing.isPresent() && existing.get().getUserId().equals(userId)) {
            BuyRecord record = existing.get();
            record.setProductName(updated.getProductName());
            record.setPrice(updated.getPrice());
            record.setDate(LocalDate.now());
            return Optional.of(repo.save(record));
        }
        return Optional.empty();
    }

    // ❌ Delete record
    public boolean deleteBuy(String id, String userId) {
        Optional<BuyRecord> existing = repo.findById(id);
        if (existing.isPresent() && existing.get().getUserId().equals(userId)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }
}
