package com.practice.auth.global.security;

import com.practice.auth.entity.Role;
import com.practice.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final RoleRepository roleRepository;

    @Override
    public CustomUserDetail loadUserByUsername(String id) throws UsernameNotFoundException {
        Long memberId = Long.parseLong(id);
        List<Role> roleList = roleRepository.findByMemberId(memberId);

        return CustomUserDetail.builder()
                .memberId(String.valueOf(memberId))
                .roles(roleList.stream()
                        .map(Role::getRole)
                        .collect(Collectors.toList()))
                .build();
    }
}
