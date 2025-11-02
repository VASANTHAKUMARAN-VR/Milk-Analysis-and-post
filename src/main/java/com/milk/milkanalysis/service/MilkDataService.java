package com.milk.milkanalysis.service;

import com.milk.milkanalysis.dto.MilkDataRequest;
import com.milk.milkanalysis.model.MilkData;
import com.milk.milkanalysis.repository.MilkDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MilkDataService {

    @Autowired
    private MilkDataRepository repo;

    // 🧩 Create new entry
    public MilkData saveMilkData(MilkDataRequest req) {
        double amount = req.getLiters() * req.getRate(); // Auto calculate
        MilkData data = MilkData.builder()
                .userId(req.getUserId())
                .session(req.getSession())
                .date(LocalDate.parse(req.getDate()))
                .liters(req.getLiters())
                .rate(req.getRate())
                .fat(req.getFat())
                .snf(req.getSnf())
                .amount(amount) // 👈 added
                .build();
        return repo.save(data);
    }

    // 🧩 Get user-specific entries
    public List<MilkData> getUserMilkData(String userId) {
        return repo.findByUserId(userId);
    }

    // 🧩 Update entry only if owned by user
    public Optional<MilkData> updateMilkData(String id, MilkDataRequest req, String userId) {
        Optional<MilkData> opt = repo.findById(id);
        if (opt.isPresent() && opt.get().getUserId().equals(userId)) {
            MilkData existing = opt.get();
            existing.setSession(req.getSession());
            existing.setDate(LocalDate.parse(req.getDate()));
            existing.setLiters(req.getLiters());
            existing.setRate(req.getRate());
            existing.setFat(req.getFat());
            existing.setSnf(req.getSnf());
            existing.setAmount(req.getLiters() * req.getRate()); // 👈 recalculate
            return Optional.of(repo.save(existing));
        }
        return Optional.empty();
    }

    // 🧩 Delete entry only if owned by user
    public boolean deleteMilkData(String id, String userId) {
        Optional<MilkData> opt = repo.findById(id);
        if (opt.isPresent() && opt.get().getUserId().equals(userId)) {
            repo.delete(opt.get());
            return true;
        }
        return false;
    }
}
