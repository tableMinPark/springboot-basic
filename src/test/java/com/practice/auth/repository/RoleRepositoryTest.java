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

import java.util.List;

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
    private final Integer ROLE_SIZE = 3;

    @BeforeEach
    void registerRole() {
        String email = "test@test.com";
        String password = "1234";

        // 회원 생성
        this.entityManager.createNativeQuery("ALTER TABLE MEMBER ALTER member_id RESTART WITH 1").executeUpdate();
        Member member = Member.builder()
                .email(email)
                .password(password)
                .build();

        memberRepository.save(member);

        // 회원 역할 생성
        this.entityManager.createNativeQuery("ALTER TABLE ROLE ALTER role_id RESTART WITH 1").executeUpdate();
        for (int i = 0; i < ROLE_SIZE; i++) {
            roleRepository.save(Role.builder()
                    .memberId(member.getMemberId())
                    .role(RoleCode.NORMAL.code)
                    .build());
        }
    }

    @DisplayName("Role 생성 테스트")
    @Test
    void roleRegisterTest() {
        assertTrue(true);
    }

    @DisplayName("Role 조회 테스트")
    @Test
    void roleFindTest() {
        List<Role> op = roleRepository.findAll();
        assertEquals(ROLE_SIZE, op.size());

        for (int i = 0; i < ROLE_SIZE; i++) {
            Role role = op.get(i);
            assertEquals(i + 1, role.getRoleId());
            assertEquals(MEMBER_ID, role.getMemberId());
            assertEquals(RoleCode.NORMAL.code, role.getRole());
        }
    }
}