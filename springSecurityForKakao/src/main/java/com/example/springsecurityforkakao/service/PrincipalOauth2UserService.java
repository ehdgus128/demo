package com.example.springsecurityforkakao.service;

import com.example.springsecurityforkakao.domain.kakao.KakaoUserInfo;
import com.example.springsecurityforkakao.domain.kakao.User;
import com.example.springsecurityforkakao.domain.kakao.auth.Oauth2UserInfo;
import com.example.springsecurityforkakao.domain.kakao.auth.PrincipalDetails;
import com.example.springsecurityforkakao.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public PrincipalOauth2UserService(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Oauth2UserInfo oauth2UserInfo = null;

        String provider = userRequest.getClientRegistration().getRegistrationId();    //google

        if(provider.equals("kakao")){	//추가
            oauth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        }

        String providerId = oAuth2User.getAttribute("sub");
        String username = provider+"_"+providerId;  			// 사용자가 입력한 적은 없지만 만들어준다

        String uuid = UUID.randomUUID().toString().substring(0, 6);
        String password = passwordEncoder.encode("패스워드"+uuid);  // 사용자가 입력한 적은 없지만 만들어준다

        String email = oAuth2User.getAttribute("email");

        User byUsername = userRepository.findByUsername(username);

        //처음 로그인한 사용자라면 회원 생성
        if (byUsername == null) {
            User user = new User(username, password, email);
            byUsername = userRepository.save(user);
        }

        return new PrincipalDetails(byUsername, oauth2UserInfo);
    }
}
