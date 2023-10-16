package com.coopbank.selfonboarding.preload;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.coopbank.selfonboarding.entity.JwtUser;
import com.coopbank.selfonboarding.entity.Role;
import com.coopbank.selfonboarding.repository.JwtUserRepository;
import com.coopbank.selfonboarding.repository.RoleRepository;
import com.coopbank.selfonboarding.service.JwtUserService;

import java.util.*;

@Component
@RequiredArgsConstructor
public class InitUsers implements CommandLineRunner {
    private final PasswordEncoder passwordEncoder;

    @Autowired
    JwtUserRepository jwtUserRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        String role1 ="ADMINISTRATOR_ROLE";
        String role2 ="API_ROLE";
        Optional<Role> roleAdmin = roleRepository.findByRoleName(role1);
        Optional<Role> roleAPI = roleRepository.findByRoleName(role2);

        if (roleAdmin.isEmpty()) {
            Role adminRole = new Role();
            adminRole.setRoleName(role1);
            adminRole.setPermissions(Arrays.asList(Role.Permissions.ROLE_ADMIN, Role.Permissions.ROLE_USERMGMT));
            roleRepository.save(adminRole);
        }

        if (roleAPI.isEmpty()) {
            Role apiRole = new Role();
            apiRole.setRoleName(role2);
            apiRole.setPermissions(Arrays.asList(Role.Permissions.ROLE_API, Role.Permissions.ROLE_USERMGMT));
            roleRepository.save(apiRole);
        }

        Optional<JwtUser> jwtUser = jwtUserRepository.findJwtUserByEmail("admin@test.com");
        Optional<JwtUser> jwtUser2 = jwtUserRepository.findJwtUserByEmail("apiuser@test.com");
        if (jwtUser==null) {

            Optional<Role>  perms = roleRepository.findByRoleName(role1);
            Role role = perms.get();
            List<Role.Permissions> permissions = role.getPermissions();


            JwtUser u = JwtUser.builder()
                    .username("Admin")
                    .email("admin@test.com")
                    .password(passwordEncoder.encode("test123"))
                    .enabled(true)
                    .roleName(role1)
                    .permissions(new HashSet<>(permissions))
                    .build();
        jwtUserRepository.save(u);
        }

        if (jwtUser2==null) {

            Optional<Role>  perms = roleRepository.findByRoleName(role2);
            Role role = perms.get();
            List<Role.Permissions> permissions = role.getPermissions();
            JwtUser u = JwtUser.builder()
                    .username("api")
                    .email("apiuser@test.com")
                    .password(passwordEncoder.encode("test123"))
                    .enabled(true)
                    .roleName(role2)
                    .permissions(new HashSet<>(permissions))
                    .build();
            jwtUserRepository.save(u);
        }

    }

}