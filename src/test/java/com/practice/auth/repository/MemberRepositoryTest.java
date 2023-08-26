package com.practice.auth.repository;

import com.practice.auth.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;
    private final String EMAIL = "test@test.com";
    private final String PASSWORD = "1234";

    @BeforeEach
    @Transactional
    void register() {
        Member member = Member.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        memberRepository.save(member);
    }

    @DisplayName("Member 생성 테스트")
    @Test
    @Transactional
    void memberRegisterTest() {
        assertTrue(true);
    }

    @DisplayName("Member 조회 테스트")
    @Test
    @Transactional
    void memberFindTest() {
        Optional<Member> op = memberRepository.findByEmail(EMAIL);
        assertTrue(op.isPresent());

        Member member = op.get();
        assertEquals(member.getEmail(), EMAIL);
        assertEquals(member.getPassword(), PASSWORD);
    }

    @DisplayName("Member 수정 테스트")
    @Test
    @Transactional
    void memberModifyTest() {
        Optional<Member> op = memberRepository.findByEmail(EMAIL);
        assertTrue(op.isPresent());

        String modifyEmail = "modify@test.com";
        String modifyPassword = "5678";

        Member member = op.get();
        member.setEmail(modifyEmail);
        member.setPassword(modifyPassword);
        memberRepository.flush();

        op = memberRepository.findByEmail(modifyEmail);
        assertTrue(op.isPresent());
    }
}