package com.melnykovmihail.security_service.data.repository;

import com.melnykovmihail.security_service.data.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    @Query("{username:'?0'}")
    Optional<User> findUserByUsername(String username);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}
