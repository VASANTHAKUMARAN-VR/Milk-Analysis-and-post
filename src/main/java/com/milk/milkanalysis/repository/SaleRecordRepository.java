package com.milk.milkanalysis.repository;

import com.milk.milkanalysis.model.SaleRecord;
import com.milk.milkanalysis.model.PostCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDate;
import java.util.List;

public interface SaleRecordRepository extends MongoRepository<SaleRecord, String> {

    List<SaleRecord> findByUserId(String userId);

    List<SaleRecord> findByCategory(PostCategory category);

    List<SaleRecord> findByDate(LocalDate date);
}
