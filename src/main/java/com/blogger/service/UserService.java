package com.blogger.service;

import java.util.Optional;

import com.blogger.entities.User;

public interface UserService {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

}
