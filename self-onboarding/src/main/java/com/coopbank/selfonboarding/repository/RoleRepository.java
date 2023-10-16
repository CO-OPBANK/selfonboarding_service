package com.coopbank.selfonboarding.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.coopbank.selfonboarding.entity.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(String roleName);

}
