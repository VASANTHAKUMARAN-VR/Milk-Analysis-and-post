package com.milk.milkanalysis.repository;

import com.milk.milkanalysis.model.CowSale;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CowSaleRepository extends MongoRepository<CowSale, String> {
    List<CowSale> findByUserId(String userId);
}
