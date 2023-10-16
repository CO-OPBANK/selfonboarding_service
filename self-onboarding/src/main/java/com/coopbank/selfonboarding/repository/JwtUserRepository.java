package com.coopbank.selfonboarding.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coopbank.selfonboarding.entity.JwtUser;

import java.util.Optional;

@Repository
public interface JwtUserRepository extends JpaRepository<JwtUser, Long> {

    Optional<JwtUser> findJwtUserByUsername(String username);
    Optional<JwtUser> findJwtUserByEmail(String email);

}
