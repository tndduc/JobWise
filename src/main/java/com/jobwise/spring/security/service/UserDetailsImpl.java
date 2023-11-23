package com.jobwise.spring.security.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jobwise.spring.model.Role;
import com.jobwise.spring.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;
/**
 * @author DucTN
 * @project JobWise-main
 * @on 11/11/2023
 */
@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {

    @Getter
    private UUID id;
    @Getter
    private String last_name;
    @Getter
    private String first_name;
    @Getter
    private Date create_at;
    @Getter
    private Date update_at;
    @Getter
    private String photo;
    @Getter
    private String address;
    @Getter
    private String phone;
    @Getter
    private String company_id;
    @Getter
    private String school_id;
    @Getter
    private String description;
    @Getter
    private String status;

    @Getter
    private String email;

    @JsonIgnore
    private String password;


    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user
                .getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getId(),
                user.getFirst_name(),
                user.getLast_name(),
                user.getCreate_at(),
                user.getUpdate_at(),
                user.getPhoto(),
                user.getAddress(),
                user.getPhone(),
                user.getCompany_id(),
                user.getSchool_id(),
                user.getDescription(),
                user.getStatus(),
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl that = (UserDetailsImpl) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, password, authorities);
    }


}
