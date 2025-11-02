package com.milk.milkanalysis.repository;

import com.milk.milkanalysis.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByMobileNumber(String mobile);
    Optional<User> findByEmailOrMobileNumber(String email, String mobile);
    boolean existsByEmail(String email);
    boolean existsByMobileNumber(String mobile);
}
