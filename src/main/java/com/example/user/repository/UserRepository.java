package com.example.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.example.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}