package com.blogger.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.blogger.entities.User;
import com.blogger.repository.UserRepository;
import com.blogger.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}