package com.practice.auth;

import com.practice.auth.entity.Member;
import com.practice.auth.entity.Role;
import com.practice.auth.entity.Session;
import com.practice.auth.global.code.RoleCode;
import com.practice.auth.global.jwt.TokenProvider;
import com.practice.auth.repository.ExpiredTokenRepository;
import com.practice.auth.repository.MemberRepository;
import com.practice.auth.repository.RoleRepository;
import com.practice.auth.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class TestUtil {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private ExpiredTokenRepository expiredTokenRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private TokenProvider tokenProvider;

    public final String EMAIL;
    public final String PASSWORD;
    public final Long MEMBER_ID;
    public String ACCESS_TOKEN;
    public String REFRESH_TOKEN;

    public TestUtil() {
        EMAIL = "test@test.com";
        PASSWORD = "12345678";
        MEMBER_ID = 1L;
        ACCESS_TOKEN = "";
        REFRESH_TOKEN = "";
    }

    public void reset() {
        this.entityManager.createNativeQuery("ALTER TABLE member ALTER member_id RESTART WITH 1").executeUpdate();
        this.entityManager.createNativeQuery("ALTER TABLE role ALTER role_id RESTART WITH 1").executeUpdate();
    }


    public void registerMember(String... roleList) {
        reset();

        String password = passwordEncoder.encode(PASSWORD);
        Member member = Member.builder()
                .email(EMAIL)
                .password(password)
                .build();
        memberRepository.save(member);

        Arrays.stream(roleList).forEach(role -> {
            roleRepository.save(Role.builder()
                    .memberId(MEMBER_ID)
                    .role(role)
                    .build());
        });
    }

    public void registerSession() {
        ACCESS_TOKEN = tokenProvider.generateAccessToken(MEMBER_ID);
        REFRESH_TOKEN = tokenProvider.generateRefreshToken(MEMBER_ID);
        Long expired = tokenProvider.getExpiredSeconds(REFRESH_TOKEN);

        sessionRepository.save(Session.builder()
                .memberId(MEMBER_ID)
                .accessToken(ACCESS_TOKEN)
                .refreshToken(REFRESH_TOKEN)
                .loginDt(LocalDateTime.now())
                .expire(expired)
                .build());
    }

    public void deleteMember() {
        memberRepository.deleteById(MEMBER_ID);
        roleRepository.deleteByMemberId(MEMBER_ID);
    }

    public void deleteSession() {
        sessionRepository.deleteById(MEMBER_ID);
    }

    public void deleteExpireToken() {
        expiredTokenRepository.deleteById(ACCESS_TOKEN);
        expiredTokenRepository.deleteById(REFRESH_TOKEN);
    }
}
