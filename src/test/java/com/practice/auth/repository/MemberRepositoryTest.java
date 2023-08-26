package com.practice.auth.repository;

import com.practice.auth.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private MemberRepository memberRepository;
    private final String EMAIL = "test@test.com";
    private final String PASSWORD = "1234";
    private final Long MEMBER_ID = 1L;

    @BeforeEach
    void register() {
        this.entityManager.createNativeQuery("ALTER TABLE MEMBER AUTO_INCREMENT = 1") .executeUpdate();
        Member member = Member.builder()
                .email(EMAIL)
                .password(PASSWORD)
                .build();

        memberRepository.save(member);

        System.out.println(member.getMemberId());
    }

    @DisplayName("Member 생성 테스트")
    @Test
    void memberRegisterTest() {
        assertTrue(true);
    }

    @DisplayName("Member 조회 테스트")
    @Test
    void memberFindTest() {
        Optional<Member> op = memberRepository.findById(MEMBER_ID);
        assertTrue(op.isPresent());

        Member member = op.get();
        assertEquals(member.getMemberId(), MEMBER_ID);
        assertEquals(member.getEmail(), EMAIL);
        assertEquals(member.getPassword(), PASSWORD);
    }

    @DisplayName("Member 수정 테스트")
    @Test
    void memberModifyTest() {
        Optional<Member> op = memberRepository.findById(MEMBER_ID);
        assertTrue(op.isPresent());

        String modifyEmail = "modify@test.com";
        String modifyPassword = "5678";

        Member member = op.get();
        member.setEmail(modifyEmail);
        member.setPassword(modifyPassword);
        memberRepository.flush();

        op = memberRepository.findById(MEMBER_ID);
        assertTrue(op.isPresent());
    }
}