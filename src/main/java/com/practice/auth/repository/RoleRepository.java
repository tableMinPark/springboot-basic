package com.practice.auth.repository;

import com.practice.auth.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByMemberId(Long memberId);
    void deleteByMemberId(Long memberId);
}
