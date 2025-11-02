package com.milk.milkanalysis.service;

import com.milk.milkanalysis.model.CowRequest;
import com.milk.milkanalysis.repository.CowRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CowRequestService {

    @Autowired
    private CowRequestRepository repo;

    // Create new request
    public CowRequest createRequest(CowRequest req) {
        return repo.save(req);
    }

    // Get all requests (visible to everyone)
    public List<CowRequest> getAllRequests() {
        return repo.findAll();
    }

    // Get user’s own requests
    public List<CowRequest> getRequestsByUser(String userId) {
        return repo.findByUserId(userId);
    }

    // Update request — only if user owns it
    public Optional<CowRequest> updateRequest(String id, String userId, CowRequest updated) {
        Optional<CowRequest> existingOpt = repo.findById(id);
        if (existingOpt.isPresent()) {
            CowRequest existing = existingOpt.get();
            if (!existing.getUserId().equals(userId)) {
                // Trying to edit someone else’s request ❌
                return Optional.empty();
            }
            existing.setUserName(updated.getUserName());
            existing.setMobileNumber(updated.getMobileNumber());
            existing.setCowBreedNeeded(updated.getCowBreedNeeded());
            existing.setMilkLitersNeeded(updated.getMilkLitersNeeded());
            existing.setPurpose(updated.getPurpose());
            existing.setLocation(updated.getLocation());
            existing.setAdditionalNotes(updated.getAdditionalNotes());
            return Optional.of(repo.save(existing));
        }
        return Optional.empty();
    }

    // Delete request — only if user owns it
    public boolean deleteRequest(String id, String userId) {
        Optional<CowRequest> existingOpt = repo.findById(id);
        if (existingOpt.isPresent() && existingOpt.get().getUserId().equals(userId)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }
}
