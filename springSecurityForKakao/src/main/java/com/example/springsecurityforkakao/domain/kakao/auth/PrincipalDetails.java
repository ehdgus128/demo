package com.example.springsecurityforkakao.domain.kakao.auth;

import com.example.springsecurityforkakao.domain.kakao.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class PrincipalDetails implements UserDetails, OAuth2User {
    private User user;
    private Oauth2UserInfo oauth2UserInfo;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    public PrincipalDetails(User user,Oauth2UserInfo oauth2UserInfo) {
        this.user = user;
        this.oauth2UserInfo = oauth2UserInfo;
    }

    public User getUser() {
        return user;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.oauth2UserInfo.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return oauth2UserInfo.getAttributes().get("sub").toString();
    }
}
