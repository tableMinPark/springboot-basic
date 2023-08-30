package com.practice.auth.service;

import com.practice.auth.dto.response.LoginResDto;
import com.practice.auth.entity.ExpiredToken;
import com.practice.auth.entity.Member;
import com.practice.auth.entity.Role;
import com.practice.auth.entity.Session;
import com.practice.auth.global.code.FailCode;
import com.practice.auth.global.code.RoleCode;
import com.practice.auth.global.exception.FailException;
import com.practice.auth.global.jwt.TokenProvider;
import com.practice.auth.repository.ExpiredTokenRepository;
import com.practice.auth.repository.MemberRepository;
import com.practice.auth.repository.RoleRepository;
import com.practice.auth.repository.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final SessionRepository sessionRepository;
    private final ExpiredTokenRepository expiredTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public void registerMember(String email, String password) throws RuntimeException {
        /* 회원 등록 가능 여부 검증 */
        if (!emailValidation(email)) {
            throw new FailException(FailCode.INVALID_EMAIL);
        }
        else if (!passwordValidation(password)) {
            throw new FailException(FailCode.INVALID_PASSWORD);
        }
        else if (memberRepository.findByEmail(email).isPresent()) {
            throw new FailException(FailCode.DUPLICATE_MEMBER);
        }

        /* 패스 워드 암호화 */
        password = passwordEncoder.encode(password);

        /* 회원 정보 저장 */
        Member member = Member.builder()
                .email(email)
                .password(password)
                .build();
        memberRepository.save(member);

        /* 역할 정보 저장 */
        Role role = Role.builder()
                .memberId(member.getMemberId())
                .role(RoleCode.NORMAL.code)
                .build();
        roleRepository.save(role);
    }

    @Transactional
    public LoginResDto login(String email, String password) throws RuntimeException {
        /* 이메일, 비밀번호 유효성 검증 */
        if (!emailValidation(email)) {
            throw new FailException(FailCode.INVALID_EMAIL);
        }
        else if (!passwordValidation(password)) {
            throw new FailException(FailCode.INVALID_PASSWORD);
        }

        /* 회원 정보 유무 확인 */
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new FailException(FailCode.NOT_FOUND_MEMBER));

        /* 비밀번호 일치 여부 확인 */
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new FailException(FailCode.INVALID_PASSWORD);
        }

        /* Redis(MemberToken) 조회해서 있으면 그대로 반환 */
        Long memberId = member.getMemberId();
        Session session = sessionRepository.findById(memberId)
                .orElseGet(() -> {
                    /* 엑세스 토큰 발급 */
                    String accessToken = tokenProvider.generateAccessToken(memberId);
                    String refreshToken = tokenProvider.generateRefreshToken(memberId);
                    Long expired = tokenProvider.getExpiredSeconds(refreshToken);

                    Session generateSession = Session.builder()
                            .memberId(memberId)
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .loginDt(LocalDateTime.now())
                            .expire(expired)
                            .build();

                    /* Redis(MemberToken) 등록 */
                    sessionRepository.save(generateSession);

                    return generateSession;
                });

        return LoginResDto.builder()
                .accessToken(session.getAccessToken())
                .refreshToken(session.getRefreshToken())
                .build();
    }

    @Transactional
    public void logout(Long memberId) {
        Session session = sessionRepository.findById(memberId)
                .orElseThrow(() -> new FailException(FailCode.NOT_FOUND_SESSION));

        ExpiredToken expiredAccessToken = getExpiredToken(session.getAccessToken());
        expiredTokenRepository.save(expiredAccessToken);
        ExpiredToken expiredRefreshToken = getExpiredToken(session.getRefreshToken());
        expiredTokenRepository.save(expiredRefreshToken);

        sessionRepository.deleteById(memberId);
    }


    private boolean emailValidation(String email) {
        boolean isOk = true;

        if (!email.contains("@")) {
            isOk = false;
        }

        return isOk;
    }

    private boolean passwordValidation(String password) {
        boolean isOk = true;

        if (password.length() < 8) {
            isOk = false;
        }

        return isOk;
    }

    private ExpiredToken getExpiredToken(String token) {
        Long expired = tokenProvider.getExpiredSeconds(token);
        Long memberId = tokenProvider.getMemberId(token);

        return ExpiredToken.builder()
                .token(token)
                .memberId(memberId)
                .expire(expired)
                .build();
    }
}
