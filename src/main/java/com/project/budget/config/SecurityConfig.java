package com.project.budget.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import com.project.budget.entity.StaffEntity;
import com.project.budget.entity.userEntity;
import com.project.budget.repository.StaffRepository;
import com.project.budget.repository.UserRepository;
import com.project.budget.service.MyUserDetailsService;

@Configuration
public class SecurityConfig {

    @Autowired
    private MyUserDetailsService myUserDetailsService;

    @Autowired
    private CustomAuthSuccessHandler customAuthSuccessHandler; // our custom handler

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/login", "/logout", "/newUser", "/css/**", "/js/**", "/images/**").permitAll()
                
                // Restrict deleted list only to IT
                .requestMatchers("/software/deleted-list").hasRole("IT")
                
                // Non-IT users can only access:
                .requestMatchers("/software/**","/dashboard").hasAnyRole("USER","IT")

                // Everything else only for IT
                .requestMatchers("/**").hasRole("IT")

                // Anything else authenticated
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .successHandler(customAuthSuccessHandler)
                .permitAll()
            )
            .userDetailsService(myUserDetailsService)
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }

    
    @Bean
    public CommandLineRunner createDefaultAdmin(UserRepository userRepository,
                                                StaffRepository staffRepository,
                                                PasswordEncoder encoder) {
        return args -> {
            // ✅ Step 1: Fetch or create the default staff
            StaffEntity defaultStaff = staffRepository.findByStaffCode("DEFAULT001")
                    .orElseGet(() -> {
                        StaffEntity newStaff = new StaffEntity();
                        newStaff.setStaffCode("DEFAULT001");
                        newStaff.setFirstName("System");
                        newStaff.setLastName("Staff");
                        newStaff.setOfficeCode("SYSTEM");
                        return staffRepository.save(newStaff);
                    });

            // ✅ Step 2: Check if the admin user exists
            userEntity existingUser = userRepository.findByUsername("admin");

            // ✅ Step 3: Create default admin if not found
            if (existingUser == null) {
                userEntity defaultUser = new userEntity();
                defaultUser.setUsername("admin");
                defaultUser.setPassword(encoder.encode("Admin@123"));
                defaultUser.setUserStatus("activeUser");
                defaultUser.setInputer("SYSTEM");
                defaultUser.setAuthoriser("SYSTEM");
                defaultUser.setDepartment("IT");
                defaultUser.setStaff(defaultStaff);

                userRepository.save(defaultUser);
                System.out.println("✅ Default admin user created: admin / Admin@123");
            } else {
                System.out.println("ℹ️ Admin user already exists");
            }
        };
    }


}
