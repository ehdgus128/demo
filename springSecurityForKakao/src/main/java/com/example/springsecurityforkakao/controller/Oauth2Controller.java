package com.example.springsecurityforkakao.controller;

import com.example.springsecurityforkakao.domain.kakao.User;
import com.example.springsecurityforkakao.domain.kakao.auth.PrincipalDetails;
import com.example.springsecurityforkakao.repository.UserRepository;
import com.example.springsecurityforkakao.service.PrincipalOauth2UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 절대로 repository를 controller에서 부르지 마시오. 테스트용 코드라 호출 한 것임.
 */
@Controller
public class Oauth2Controller {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public Oauth2Controller(UserRepository userRepository, PasswordEncoder passwordEncoder, PrincipalOauth2UserService principalOauth2UserService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/loginForm")
    public String loginForm(){
        return "login";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "join";
    }

    @GetMapping("/fail")
    @ResponseBody
    public String loginFail() {
        return "너 로그인 실패했어";
    }

    @PostMapping("/join")
    public String join(@ModelAttribute User user){
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        userRepository.save(user);  //반드시 패스워드 암호화해야함
        return "redirect:/loginForm";
    }

    @GetMapping("/oauth/loginInfo")
    @ResponseBody
    public String oauthLoginInfo(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth2UserPrincipal){
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        System.out.println(attributes);
        // PrincipalOauth2UserService의 getAttributes내용과 같음

        Map<String, Object> attributes1 = oAuth2UserPrincipal.getAttributes();
        // attributes == attributes1

        return attributes.toString();     //세션에 담긴 user가져올 수 있음음
    }

    @GetMapping("/loginInfo")
    @ResponseBody
    public String loginInfo(Authentication authentication, @AuthenticationPrincipal PrincipalDetails principalDetails){
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        return "OAuth2 로그인 : " + principal;
    }
}
