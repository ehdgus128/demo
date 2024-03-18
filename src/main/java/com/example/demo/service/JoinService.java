package com.example.demo.service;

import com.example.demo.dto.JoinDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class JoinService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void joinProcess(JoinDTO joinDTO){

        System.out.println("==== Start Transaction ====");

        //db에 이미 동일한 username을 가진 회원이 존재하는지?

        UserEntity data = new UserEntity();
        System.out.println("username: " + joinDTO.getUsername());
        System.out.println("password: " + joinDTO.getPassword());

        data.setUsername(joinDTO.getUsername());
        data.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword()));
        data.setRole("ROLE_USER");

        System.out.println("role: " + data.getRole());

        userRepository.save(data);

        System.out.println("==== End Transaction ====");
    }
}
