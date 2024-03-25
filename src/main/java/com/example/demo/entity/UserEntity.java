package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;


@Entity
@Getter
@Setter
@IdClass(UserEntityPK.class)
public class UserEntity {

    // email, type 컬럼을 pk로 사용
    @Id
    private String email;

    @Id
    private String type;

    private String role;
    private String name;
    private String gender;
    private String birthday;
    private String phoneNumber;
}

class UserEntityPK implements Serializable {
    private String email;
    private String type;
    // equals와 hashCode 구현 필요
}
