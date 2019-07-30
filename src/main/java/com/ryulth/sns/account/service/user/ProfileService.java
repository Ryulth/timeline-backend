package com.ryulth.sns.account.service.user;

import com.ryulth.sns.account.dto.ProfileImageDto;
import com.ryulth.sns.account.dto.UserEditDto;
import com.ryulth.sns.account.dto.UserInfoDto;
import com.ryulth.sns.account.entity.User;
import com.ryulth.sns.account.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class ProfileService {
    private final UserRepository userRepository;

    public ProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserInfoDto getProfile(String email){
        User user =userRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);
        return getUserInfoDtoByUser(user);
    }

    public UserInfoDto editProfile(String email, UserEditDto userEditDto){
        User user =userRepository.findByEmail(email)
                .orElseThrow(EntityNotFoundException::new);

        User editUser = User.builder()
                .id(user.getId())
                .email(user.getEmail())
                .password(user.getPassword())
                .username(user.getUsername())
                .state(userEditDto.getState())
                .school(userEditDto.getSchool())
                .birth(userEditDto.getBirth())
                .thumbImageUrl(userEditDto.getProfileImageDto().getThumbUrl())
                .imageUrl(userEditDto.getProfileImageDto().getUrl())
                .build();

        userRepository.save(editUser);
        return getUserInfoDtoByUser(editUser);
    }

    private UserInfoDto getUserInfoDtoByUser(User user){
        return UserInfoDto.builder()
                .email(user.getEmail())
                .school(user.getSchool())
                .state(user.getState())
                .username(user.getUsername())
                .birth(user.getBirth())
                .profileImageDto(ProfileImageDto.builder()
                        .url(user.getImageUrl())
                        .thumbUrl(user.getThumbImageUrl())
                        .build())
                .build();
    }
}
