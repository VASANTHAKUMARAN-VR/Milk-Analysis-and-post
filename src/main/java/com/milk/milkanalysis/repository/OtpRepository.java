package com.milk.milkanalysis.repository;

import com.milk.milkanalysis.model.Otp;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OtpRepository extends MongoRepository<Otp, String> {

    Optional<Otp> findTopByEmailAndUsedOrderByExpiryTimeDesc(String email, boolean used);

    Optional<Otp> findByCode(String code);  // ✅ added inside the interface
}
