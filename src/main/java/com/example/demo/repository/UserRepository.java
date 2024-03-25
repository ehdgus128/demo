package com.example.demo.repository;

import com.example.demo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserRepository  extends JpaRepository<UserEntity, Integer> {

//    boolean existsByUsername(String username);
//
//    UserEntity findByUsername(String username);
}
