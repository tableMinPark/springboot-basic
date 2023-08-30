package com.practice.auth.service;

import com.practice.auth.TestUtil;
import com.practice.auth.entity.Member;
import com.practice.auth.entity.Role;
import com.practice.auth.global.code.RoleCode;
import com.practice.auth.repository.MemberRepository;
import com.practice.auth.repository.RoleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RegisterMemberTest {
    @Autowired
    private TestUtil testUtil;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @DisplayName("회원 가입 단위 테스트")
    @Test
    @Transactional
    void 회원_가입_단위_테스트() {
        testUtil.reset();

        // when
        /* 회원 등록 가능 여부 검증 */
        if (!emailValidation(testUtil.EMAIL)) {
            fail();
        }
        else if (!passwordValidation(testUtil.PASSWORD)) {
            fail();
        }
        else if (memberRepository.findByEmail(testUtil.EMAIL).isPresent()) {
            fail();
        }

        /* 패스 워드 암호화 */
        String password = passwordEncoder.encode(testUtil.PASSWORD);

        /* 회원 정보 저장 */
        Member member = Member.builder()
                .email(testUtil.EMAIL)
                .password(password)
                .build();
        memberRepository.save(member);

        /* 역할 정보 저장 */
        Role role = Role.builder()
                .memberId(member.getMemberId())
                .role(RoleCode.NORMAL.code)
                .build();
        roleRepository.save(role);

        // then
        Optional<Member> memberOptional = memberRepository.findById(testUtil.MEMBER_ID);
        assertTrue(memberOptional.isPresent());
        member = memberOptional.get();
        assertEquals(testUtil.MEMBER_ID, member.getMemberId());
        assertEquals(testUtil.EMAIL, member.getEmail());
        assertEquals(password, member.getPassword());

        List<Role> roleList = roleRepository.findByMemberId(member.getMemberId());
        assertEquals(1, roleList.size());

        testUtil.deleteMember();
    }

    @DisplayName("비밀번호 암호화 단위 테스트")
    @Test
    void 비밀번호_암호화_단위_테스트() {
        String password = "12345678";
        String encodePassword = passwordEncoder.encode(password);

        assertNotEquals(password, encodePassword);
        assertTrue(passwordEncoder.matches(password, encodePassword));
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