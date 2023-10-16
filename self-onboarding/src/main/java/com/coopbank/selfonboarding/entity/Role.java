package com.coopbank.selfonboarding.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String roleName;

    @ElementCollection(targetClass = Permissions.class,fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<Permissions> permissions;

    public enum Permissions {
        ROLE_USER,
        ROLE_USERMGMT,
        ROLE_RM,
        ROLE_ADMIN,
        ROLE_SACCO,
        ROLE_GOVT,
        ROLE_SERVICEDESK,
        ROLE_API
    }

    public List<Permissions> getPermissions() {
        return this.permissions;
    }

    public void setPermissions(List<Permissions> permissions) {
        this.permissions = permissions;
    }

    // getters and setters for id and roleName
}
