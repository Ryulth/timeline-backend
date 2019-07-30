package com.ryulth.sns.account.service.user;

import com.ryulth.sns.account.dto.LoginDto;
import com.ryulth.sns.account.dto.RegisterDto;
import com.ryulth.sns.account.dto.TokenDto;
import com.ryulth.sns.account.entity.User;
import com.ryulth.sns.account.exception.EmailNotFoundException;
import com.ryulth.sns.account.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
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

    public TokenDto register(RegisterDto registerDto) {
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
                .thumbImageUrl(registerDto.getProfileImageDto().getThumbUrl())
                .imageUrl(registerDto.getProfileImageDto().getUrl())
                .build();

        userRepository.save(user);
        return getToken(user);
    }

    public boolean duplicateEmail(String userEmail) {
        return userRepository.existsByEmail(userEmail);
    }

    public TokenDto login(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(EmailNotFoundException::new);
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Incorrect password");
        }
        return getToken(user);
    }

    public TokenDto updateAccessToken(String refreshToken) throws IllegalAccessException {
        String email = tokenService.getEmailFromRefreshToken(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(EmailNotFoundException::new);

        return getToken(user);
    }

    private TokenDto getToken(User user) {
        Map<String, Object> body = Stream.of(
                new AbstractMap.SimpleEntry<>("email", user.getEmail()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        return tokenService.publishToken(body, "userInfo");
    }

}
