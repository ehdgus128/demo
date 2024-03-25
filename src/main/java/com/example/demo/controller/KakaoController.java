package com.example.demo.controller;

import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UserRepository;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/oauth2/kakao")
public class KakaoController {

    private final UserRepository userRepository;

    public KakaoController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //redirect uri에 전달된 코드 값을 가지고 Access Token을 요청한다.
    @GetMapping
    public String getAccessToken(@RequestParam("code") String code, Model model) {
        System.out.println("code = " + code);

        // 1. header 생성
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded;charset=utf-8");

        System.out.println("222 = " + 222);
        // 2. body 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code"); //고정값
        params.add("client_id", "603b6f523013db5e0b8e02d2dfa39dc6");
        params.add("redirect_uri", "http://localhost:8080/oauth2/kakao"); //등록한 redirect uri
        params.add("code", code);

        // 3. header + body
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, httpHeaders);
        // 4. http 요청하기
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response = restTemplate.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                httpEntity,
                String.class
        );

        String responseBody = response.getBody();
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(responseBody, JsonObject.class);
        String accessToken = jsonObject.get("access_token").getAsString();

        JsonObject userInfo = getUserInfo(accessToken);

        String nickname = userInfo.getAsJsonObject("properties").get("nickname").getAsString();
        String email = userInfo.getAsJsonObject("kakao_account").get("email").getAsString();
        String name = userInfo.getAsJsonObject("kakao_account").get("name").getAsString();
        String gender = userInfo.getAsJsonObject("kakao_account").get("gender").getAsString();
        String birthday = userInfo.getAsJsonObject("kakao_account").get("birthyear").getAsString() + userInfo.getAsJsonObject("kakao_account").get("birthday").getAsString();
        String phone_number = userInfo.getAsJsonObject("kakao_account").get("phone_number").getAsString();

        System.out.println("======================Access Token: " + accessToken);
        System.out.println("======================nickname: " + nickname);
        System.out.println("======================email: " + email);
        System.out.println("======================name: " + name);
        System.out.println("======================gender: " + gender);
        System.out.println("======================birthday: " + birthday);
        System.out.println("======================phone_number: " + phone_number);


        System.out.println("response = " + response);

        model.addAttribute("accessToken", accessToken);
        model.addAttribute("kakaoClientId", "603b6f523013db5e0b8e02d2dfa39dc6");
        model.addAttribute("redirectUri", "http://localhost:8080/oauth2/kakao");
        model.addAttribute("code", code);
        model.addAttribute("name", name);
        model.addAttribute("email", email);
        model.addAttribute("gender", gender);
        model.addAttribute("birthday", birthday);
        model.addAttribute("phone_number", phone_number);


        // 사용자 정보 저장
        UserEntity userEntity = new UserEntity();
        userEntity.setType("KAKAO");
        userEntity.setRole("USER");
        userEntity.setName(name);
        userEntity.setEmail(email);
        userEntity.setGender(gender);
        userEntity.setBirthday(birthday);
        userEntity.setPhoneNumber(phone_number);
        userRepository.save(userEntity);

        return "home";
    }

    private JsonObject getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                entity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            String userInfoResponse = response.getBody();
            Gson gson = new Gson();
            return gson.fromJson(userInfoResponse, JsonObject.class);
        } else {
            return null;
        }
    }

}