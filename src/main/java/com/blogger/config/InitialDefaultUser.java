package com.blogger.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.blogger.entities.User;
import com.blogger.enums.ROLES;
import com.blogger.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class InitialDefaultUser implements CommandLineRunner {

	private final UserRepository userInfoRepo;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {

        User admin = new User();
        admin.setUsername("Admin");
        admin.setPassword(passwordEncoder.encode("password"));
        admin.setRoles(ROLES.ROLE_ADMIN.toString());
        admin.setEmail("admin@admin.com");

        User user = new User();
        user.setUsername("User");
        user.setPassword(passwordEncoder.encode("password"));
        user.setRoles(ROLES.ROLE_BLOGGER.toString());
        user.setEmail("user@user.com");

        userInfoRepo.saveAll(List.of(admin,user));
    }
    
}
