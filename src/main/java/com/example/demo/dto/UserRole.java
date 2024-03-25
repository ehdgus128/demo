package com.example.demo.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {

    USER("USER", "일반사용자"),
    ADMIN("ADMIN", "일반관리자");

    private final String key;
    private final String title;

}