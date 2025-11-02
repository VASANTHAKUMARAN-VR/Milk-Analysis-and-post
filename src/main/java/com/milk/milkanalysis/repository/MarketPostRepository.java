package com.milk.milkanalysis.repository;

import com.milk.milkanalysis.model.MarketPost;
import com.milk.milkanalysis.model.PostCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MarketPostRepository extends MongoRepository<MarketPost, String> {
    List<MarketPost> findByMobileNumber(String mobileNumber);
    List<MarketPost> findByCategory(PostCategory category);
}
