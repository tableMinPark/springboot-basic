package com.practice.auth.repository;


import com.practice.auth.entity.ExpiredToken;
import org.springframework.data.repository.CrudRepository;

public interface ExpiredTokenRepository extends CrudRepository<ExpiredToken, String> {
}
