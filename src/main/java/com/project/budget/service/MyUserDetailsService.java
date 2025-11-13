package com.project.budget.service;

import com.project.budget.config.CustomUserDetails;
import com.project.budget.entity.userEntity;
import com.project.budget.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        userEntity user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        System.out.println("Logged in user: " + user.getUsername() + ", Department: " + user.getDepartment());

        // Dynamic role assignment
        String role = "ROLE_USER";
        if ("Information Technology Department".equalsIgnoreCase(user.getDepartment())) {
            role = "ROLE_IT";
        }

        Collection<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(role);

        // Return CustomUserDetails (acts like User + custom fields)
        return new CustomUserDetails(user, authorities);
    }
}
