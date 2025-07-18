package com.gaurav.BasicSpringSecurity.repository;

import com.gaurav.BasicSpringSecurity.model.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends MongoRepository<UserEntity, String> {
    public Optional<UserEntity> findByUsername(String username);
    public void deleteByUsername(String username);
    public Optional<UserEntity> findByEmail(String email);
    public Optional<UserEntity> findByResetToken(String token);
}
