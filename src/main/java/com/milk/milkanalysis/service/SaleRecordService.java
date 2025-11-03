package com.milk.milkanalysis.service;

import com.milk.milkanalysis.model.SaleRecord;
import com.milk.milkanalysis.model.PostCategory;
import com.milk.milkanalysis.repository.SaleRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SaleRecordService {

    @Autowired
    private SaleRecordRepository repo;

    // ➕ Add Sale Record
    public SaleRecord addSale(SaleRecord record) {
        record.setDate(LocalDate.now());
        return repo.save(record);
    }

    // 👤 Get Sales by User
    public List<SaleRecord> getSalesByUser(String userId) {
        return repo.findByUserId(userId);
    }

    // 🏷️ Get Sales by Category
    public List<SaleRecord> getSalesByCategory(PostCategory category) {
        return repo.findByCategory(category);
    }

    // 🗓️ Get Sales by Date
    public List<SaleRecord> getSalesByDate(LocalDate date) {
        return repo.findByDate(date);
    }

    // ✏️ Update Sale
    public Optional<SaleRecord> updateSale(String id, SaleRecord updated) {
        Optional<SaleRecord> existing = repo.findById(id);
        if (existing.isPresent()) {
            SaleRecord record = existing.get();

            if (updated.getCategory() != null) {
                record.setCategory(updated.getCategory());
            }

            if (updated.getPrice() != 0) { // ✅ fixed null-safe double check
                record.setPrice(updated.getPrice());
            }

            record.setDate(LocalDate.now());
            return Optional.of(repo.save(record));
        }
        return Optional.empty();
    }

    // ❌ Delete Sale
    public boolean deleteSale(String id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }
}
