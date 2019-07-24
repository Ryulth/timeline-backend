package com.ryulth.sns.account.service;

import com.ryulth.sns.account.dto.UserInfoDto;
import com.ryulth.sns.account.entity.User;
import com.ryulth.sns.account.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.Map;

@Service
public class ProfileService {
    private final UserRepository userRepository;

    public ProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Map<String, Object> getProfile(String email){
        User user =userRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);

        UserInfoDto userInfoDto = UserInfoDto.builder()
                .email(user.getEmail())
                .school(user.getSchool())
                .state(user.getState())
                .username(user.getUsername())
                .birth(user.getBirth())
                .imageUrl(user.getImageUrl())
                .build();

        return Collections.singletonMap("user",userInfoDto);
    }
}
