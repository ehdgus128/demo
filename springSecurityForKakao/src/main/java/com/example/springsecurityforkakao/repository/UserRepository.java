package com.example.springsecurityforkakao.repository;

import com.example.springsecurityforkakao.domain.kakao.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
