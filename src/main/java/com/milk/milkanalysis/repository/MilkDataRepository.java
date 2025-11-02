package com.milk.milkanalysis.repository;

import com.milk.milkanalysis.model.MilkData;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MilkDataRepository extends MongoRepository<MilkData, String> {
    List<MilkData> findByUserId(String userId);
}
