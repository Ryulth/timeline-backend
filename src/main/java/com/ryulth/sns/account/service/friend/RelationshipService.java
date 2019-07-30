package com.ryulth.sns.account.service.friend;

import com.ryulth.sns.account.dto.FriendsDto;
import com.ryulth.sns.account.dto.ProfileImageDto;
import com.ryulth.sns.account.entity.User;
import com.ryulth.sns.account.repository.UserRepository;
import com.ryulth.sns.account.dto.FriendInfoDto;
import com.ryulth.sns.account.entity.Relationship;
import com.ryulth.sns.account.entity.RelationshipStatus;
import com.ryulth.sns.account.repository.RelationshipRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RelationshipService {
    private final UserRepository userRepository;
    private final RelationshipRepository relationshipRepository;

    public RelationshipService(UserRepository userRepository, RelationshipRepository relationshipRepository1) {
        this.userRepository = userRepository;
        this.relationshipRepository = relationshipRepository1;
    }

    public FriendsDto getSendFriendInfoByRelationships(List<Relationship> relationships) {
        List<FriendInfoDto> friendInfoDtos = new ArrayList<>();

        for (Relationship relationship : relationships) {
            User user = userRepository.findByEmail(
                    relationship.getRequestEmail()
            ).orElse(null);
            if (user != null) {
                friendInfoDtos.add(
                        userToFriendInfoDto(user, relationship.getRelationshipStatus())
                );
            }

        }
        return FriendsDto.builder()
                .friendInfoDtos(friendInfoDtos)
                .build();
    }

    public FriendsDto getReceiveFriendInfoByRelationships(List<Relationship> relationships) {
        List<FriendInfoDto> friendInfoDtos = new ArrayList<>();

        for (Relationship relationship : relationships) {
            User user = userRepository.findByEmail(
                    relationship.getUserEmail()
            ).orElse(null);
            if (user != null) {
                friendInfoDtos.add(
                        userToFriendInfoDto(user, relationship.getRelationshipStatus())
                );
            }

        }
        return FriendsDto.builder()
                .friendInfoDtos(friendInfoDtos)
                .build();
    }

    private FriendInfoDto userToFriendInfoDto(User user, RelationshipStatus relationshipStatus) {
        return FriendInfoDto.builder()
                .profileImageDto(ProfileImageDto.builder()
                        .url(user.getImageUrl())
                        .thumbUrl(user.getThumbImageUrl())
                        .build())
                .email(user.getEmail())
                .username(user.getUsername())
                .state(user.getState())
                .school(user.getSchool())
                .birth(user.getBirth())
                .relationshipStatus(relationshipStatus)
                .build();
    }

    public FriendsDto getFriendInfoByUserEmailAndUsers(String userEmail, List<User> usersByState) {
        List<FriendInfoDto> friendInfoDtos = new ArrayList<>();
        for (User user : usersByState) {
            RelationshipStatus relationshipStatus = getRelationshipStatus(userEmail, user.getEmail());
            if (relationshipStatus.equals(RelationshipStatus.NONE)) {
                friendInfoDtos.add(
                        FriendInfoDto.builder()
                                .profileImageDto(ProfileImageDto.builder()
                                        .url(user.getImageUrl())
                                        .thumbUrl(user.getThumbImageUrl())
                                        .build())
                                .email(user.getEmail())
                                .username(user.getUsername())
                                .state(user.getState())
                                .school(user.getSchool())
                                .birth(user.getBirth())
                                .relationshipStatus(relationshipStatus)
                                .build()
                );
            }

        }
        return FriendsDto.builder()
                .friendInfoDtos(friendInfoDtos)
                .build();
    }

    //TODO validation check logic refactor
    private RelationshipStatus getRelationshipStatus(String userEmail, String friendEmail) {
        Relationship relationship =
                relationshipRepository.findByUserEmailAndRequestEmail(userEmail, friendEmail).orElse(null);
        if (relationship == null) {
            Relationship friendRelationship =
                    relationshipRepository.findByUserEmailAndRequestEmail(friendEmail, userEmail).orElse(null);
            if (friendRelationship == null) {
                return RelationshipStatus.NONE;
            }
            return friendRelationship.getRelationshipStatus();
        }
        return relationship.getRelationshipStatus();
    }

    public void refuseRelationshipStatus(Relationship relationship){
        relationship.setRelationshipStatus(RelationshipStatus.REFUSE);
        relationshipRepository.save(relationship);
    }

    @CacheEvict(value = "friends", key = "#relationship.getUserEmail()")
    public void friendRelationshipStatus(Relationship relationship){
        relationship.setRelationshipStatus(RelationshipStatus.FRIEND);
        relationshipRepository.save(relationship);
    }
}
