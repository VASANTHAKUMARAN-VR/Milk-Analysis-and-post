package com.milk.milkanalysis.repository;

import com.milk.milkanalysis.model.BuyRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface BuyRecordRepository extends MongoRepository<BuyRecord, String> {
    List<BuyRecord> findByUserId(String userId);
}
