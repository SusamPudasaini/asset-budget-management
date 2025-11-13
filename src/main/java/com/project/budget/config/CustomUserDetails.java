package com.project.budget.config;

import com.project.budget.entity.userEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomUserDetails extends User {

    private final userEntity user;

    public CustomUserDetails(userEntity user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getUsername(), user.getPassword(), authorities);
        this.user = user;
    }

    public String getDepartment() {
        return user.getDepartment();
    }

    public Long getId() {
        return user.getId();
    }

    public userEntity getUserEntity() {
        return user;
    }
}
