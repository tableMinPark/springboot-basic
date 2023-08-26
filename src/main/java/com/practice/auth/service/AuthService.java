package com.practice.auth.service;

import com.practice.auth.entity.Member;
import com.practice.auth.entity.Role;
import com.practice.auth.global.code.FailCode;
import com.practice.auth.global.code.RoleCode;
import com.practice.auth.global.exception.FailException;
import com.practice.auth.repository.MemberRepository;
import com.practice.auth.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

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
}
