package com.milk.milkanalysis.repository;

import com.milk.milkanalysis.model.CowPurchase;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CowPurchaseRepository extends MongoRepository<CowPurchase, String> {
    List<CowPurchase> findByUserId(String userId);
}
