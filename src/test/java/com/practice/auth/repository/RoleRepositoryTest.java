package com.practice.auth.repository;

import com.practice.auth.code.RoleCode;
import com.practice.auth.entity.Member;
import com.practice.auth.entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application-test.yml")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoleRepositoryTest {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RoleRepository roleRepository;
    private final Long MEMBER_ID = 1L;

    @BeforeEach
    void registerRole() {
        String email = "test@test.com";
        String password = "1234";

        // 회원 생성
        this.entityManager.createNativeQuery("ALTER TABLE MEMBER AUTO_INCREMENT = 1").executeUpdate();
        Member member = Member.builder()
                .email(email)
                .password(password)
                .build();

        memberRepository.save(member);

        Role role = Role.builder()
                .memberId(member.getMemberId())
                .role(RoleCode.NORMAL.code)
                .build();

        roleRepository.save(role);
    }

    @DisplayName("Role 생성 테스트")
    @Test
    void roleRegisterTest() {
        assertTrue(true);
    }
}