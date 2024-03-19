package com.example.demo.service;

import com.example.demo.dto.JoinDTO;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class JoinService {

    private static final Logger logger = LoggerFactory.getLogger(JoinService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

   @Transactional
    public void joinProcess (JoinDTO joinDTO){

        System.out.println("==== Start Transaction ====");

        UserEntity data = new UserEntity();
        System.out.println("username: " + joinDTO.getUsername());
        System.out.println("password: " + joinDTO.getPassword());

        data.setUsername(joinDTO.getUsername());
        data.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword()));
        data.setRole("ROLE_USER");

        System.out.println("role: " + data.getRole());

//        userRepository.save(data);
       try {
           userRepository.save(data);
       } catch (DataAccessException e) {
           // 예외 메시지를 출력하고 로깅합니다.
           System.err.println("Error occurred while saving data to database: " + e.getMessage());
           logger.error("Error occurred while saving data to database", e);
           // 예외 처리를 위한 추가적인 작업을 수행할 수 있습니다.
       }

        System.out.println("==== End Transaction ====");
    }
}
