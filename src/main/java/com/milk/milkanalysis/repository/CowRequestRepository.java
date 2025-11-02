package com.milk.milkanalysis.repository;

import com.milk.milkanalysis.model.CowRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CowRequestRepository extends MongoRepository<CowRequest, String> {
    List<CowRequest> findByUserId(String userId);
}
