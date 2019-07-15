package com.ryulth.timeline.account.service;

import com.ryulth.timeline.account.dto.RegisterDto;
import com.ryulth.timeline.account.entity.User;
import com.ryulth.timeline.account.repository.UserRepository;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AccountService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AccountService(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    public Map<String, Object> register(RegisterDto registerDto) {
        if (duplicateEmail(registerDto.getEmail())) {
            throw new EntityExistsException("Email is duplicated");
        }

        User user = User.builder()
                .email(registerDto.getEmail())
                .username(registerDto.getUsername())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .state(registerDto.getState())
                .school(registerDto.getSchool())
                .birth(registerDto.getBirth())
                .build();

        userRepository.save(user);
        return getToken(user);
    }

    public boolean duplicateEmail(String userEmail){
        return userRepository.existsByEmail(userEmail);
    }

    private Map<String, Object> getToken(User user) {
        Map<String, Object> body = Stream.of(
                new AbstractMap.SimpleEntry<>("email", user.getEmail()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return tokenService.publishToken(body, "userInfo");
    }

}
