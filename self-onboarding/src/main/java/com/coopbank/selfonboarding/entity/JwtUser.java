package com.coopbank.selfonboarding.entity;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.coopbank.selfonboarding.entity.Role.Permissions;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@EqualsAndHashCode(of = "uuid")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class JwtUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    private String uuid = UUID.randomUUID().toString();

    @Column
    private String username;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role.Permissions> permissions = new HashSet<>();

    @Column
    @Builder.Default
    private boolean enabled = false;

    @OneToOne(mappedBy = "user")
    private RefreshToken refreshToken;


    @Column
    private String roleName;

//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        Set<GrantedAuthority> authorities = new HashSet<>();
//        authorities.addAll(this.getRole().getAuthorities());
//        return authorities;
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	Set<GrantedAuthority> authorities = new HashSet<>();
        for (Permissions permission : this.permissions) {
            var sga = new SimpleGrantedAuthority(permission.name());
            authorities.add(sga);
        }
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
}