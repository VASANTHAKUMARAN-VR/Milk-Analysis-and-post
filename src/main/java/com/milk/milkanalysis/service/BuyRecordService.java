package com.milk.milkanalysis.service;

import com.milk.milkanalysis.model.BuyRecord;
import com.milk.milkanalysis.model.User;
import com.milk.milkanalysis.repository.BuyRecordRepository;
import com.milk.milkanalysis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BuyRecordService {

    @Autowired
    private BuyRecordRepository repo;

    @Autowired
    private UserRepository userRepo; // ✅ for email/mobile linking

    // 🔍 Utility method to resolve userId (works for email or mobile)
    private Optional<String> resolveUserId(String userIdentifier) {
        Optional<User> userOpt = userRepo.findByEmailOrMobileNumber(userIdentifier, userIdentifier);
        return userOpt.map(User::getId);
    }

    // ➕ Add record
    public BuyRecord addBuy(BuyRecord record) {
        record.setDate(LocalDate.now());
        return repo.save(record);
    }

    // 👤 Get all records by user (supports email or mobile)
    public List<BuyRecord> getUserBuys(String userIdentifier) {
        Optional<String> userIdOpt = resolveUserId(userIdentifier);
        if (userIdOpt.isEmpty()) return List.of();
        return repo.findByUserId(userIdOpt.get());
    }

    // ✏️ Update record (supports email or mobile)
    public Optional<BuyRecord> updateBuy(String id, String userIdentifier, BuyRecord updated) {
        Optional<String> userIdOpt = resolveUserId(userIdentifier);
        if (userIdOpt.isEmpty()) return Optional.empty();

        String userId = userIdOpt.get();
        Optional<BuyRecord> existing = repo.findById(id);
        if (existing.isPresent() && existing.get().getUserId().equals(userId)) {
            BuyRecord record = existing.get();

            if (updated.getProductName() != null && !updated.getProductName().isEmpty()) {
                record.setProductName(updated.getProductName());
            }
            if (updated.getPrice() != 0) {
                record.setPrice(updated.getPrice());
            }

            record.setDate(LocalDate.now());
            return Optional.of(repo.save(record));
        }
        return Optional.empty();
    }

    // ❌ Delete record (supports email or mobile)
    public boolean deleteBuy(String id, String userIdentifier) {
        Optional<String> userIdOpt = resolveUserId(userIdentifier);
        if (userIdOpt.isEmpty()) return false;

        String userId = userIdOpt.get();
        Optional<BuyRecord> existing = repo.findById(id);
        if (existing.isPresent() && existing.get().getUserId().equals(userId)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }
}
