package com.practice.auth.global.security;

import lombok.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Builder
public class CustomUserDetail implements UserDetails {
    private String memberId;
    private List<String> roles;

    public static Long getMemberId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 익명 계정 확인
        if (authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        CustomUserDetail customUserDetail = (CustomUserDetail) authentication.getPrincipal();
        String memberId = customUserDetail.getUsername();
        return Long.parseLong(memberId);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return memberId;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
