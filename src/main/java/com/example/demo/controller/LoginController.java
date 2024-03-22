package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginP() {

        return "login";
    }

    @GetMapping("/kakaoLogin")
    public String kakaoLoginP() {

        return "kakaoLogin";
    }


}
