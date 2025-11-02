package com.milk.milkanalysis.repository;

import com.milk.milkanalysis.model.CowSalePost;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CowSalePostRepository extends MongoRepository<CowSalePost, String> {
    List<CowSalePost> findByMobileNumber(String mobileNumber);
}
