package com.milk.milkanalysis.service;

import com.milk.milkanalysis.dto.MilkDataRequest;
import com.milk.milkanalysis.model.MilkData;
import com.milk.milkanalysis.repository.MilkDataRepository;
import com.milk.milkanalysis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MilkDataService {

    @Autowired
    private MilkDataRepository repo;

    @Autowired
    private UserRepository userRepository;

    // 🧩 Create new entry
    public MilkData saveMilkData(MilkDataRequest req) {
        // Find user using either email or mobile
        var userOpt = userRepository.findByEmailOrMobileNumber(req.getUserId(), req.getUserId());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found for given identifier: " + req.getUserId());
        }

        // Always use mobile number as the unique user ID
        String actualUserId = userOpt.get().getMobileNumber();

        // Calculate amount
        double amount = req.getLiters() * req.getRate();

        MilkData data = MilkData.builder()
                .userId(actualUserId) // 👈 Always mobile-based ID
                .session(req.getSession())
                .date(LocalDate.parse(req.getDate()))
                .liters(req.getLiters())
                .rate(req.getRate())
                .fat(req.getFat())
                .snf(req.getSnf())
                .amount(amount)
                .build();

        return repo.save(data);
    }

    // 🧩 Get user-specific entries
    public List<MilkData> getUserMilkData(String identifier) {
        // identifier = email or mobile entered during login
        var userOpt = userRepository.findByEmailOrMobileNumber(identifier, identifier);
        if (userOpt.isEmpty()) {
            return List.of(); // no user found
        }

        // Always use mobile number as the real ID
        String actualUserId = userOpt.get().getMobileNumber();
        return repo.findByUserId(actualUserId);
    }

    // 🧩 Update entry only if owned by user
    public Optional<MilkData> updateMilkData(String id, MilkDataRequest req, String userId) {
        Optional<MilkData> opt = repo.findById(id);

        if (opt.isPresent()) {
            MilkData existing = opt.get();

            // Resolve actual user ID
            var userOpt = userRepository.findByEmailOrMobileNumber(userId, userId);
            if (userOpt.isEmpty()) {
                return Optional.empty();
            }

            String actualUserId = userOpt.get().getMobileNumber();

            // Only allow update if entry belongs to the same user
            if (existing.getUserId().equals(actualUserId)) {
                existing.setSession(req.getSession());
                existing.setDate(LocalDate.parse(req.getDate()));
                existing.setLiters(req.getLiters());
                existing.setRate(req.getRate());
                existing.setFat(req.getFat());
                existing.setSnf(req.getSnf());
                existing.setAmount(req.getLiters() * req.getRate()); // recalculate
                return Optional.of(repo.save(existing));
            }
        }
        return Optional.empty();
    }

    // 🧩 Delete entry only if owned by user
    public boolean deleteMilkData(String id, String userId) {
        Optional<MilkData> opt = repo.findById(id);

        if (opt.isPresent()) {
            var userOpt = userRepository.findByEmailOrMobileNumber(userId, userId);
            if (userOpt.isEmpty()) {
                return false;
            }

            String actualUserId = userOpt.get().getMobileNumber();

            if (opt.get().getUserId().equals(actualUserId)) {
                repo.delete(opt.get());
                return true;
            }
        }
        return false;
    }
}
