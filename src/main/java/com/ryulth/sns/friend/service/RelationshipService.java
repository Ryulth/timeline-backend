package com.ryulth.sns.friend.service;

import com.ryulth.sns.account.entity.User;
import com.ryulth.sns.account.repository.UserRepository;
import com.ryulth.sns.friend.dto.FriendInfoDto;
import com.ryulth.sns.friend.entity.Relationship;
import com.ryulth.sns.friend.entity.RelationshipStatus;
import com.ryulth.sns.friend.repository.RelationshipRepository;
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

    public List getSendFriendInfoByRelationships(List<Relationship> relationships) {
        List<FriendInfoDto> friendInfoDtos = new ArrayList<>();

        for (Relationship relationship : relationships) {
            User user = userRepository.findByEmail(
                    relationship.getRequestEmail()
            ).orElse(null);
            if (user != null) {
                friendInfoDtos.add(
                        userToFriendInfoDto(user,relationship.getRelationshipStatus())
                );
            }

        }
        return friendInfoDtos;
    }

    public List getReceiveFriendInfoByRelationships(List<Relationship> relationships) {
        List<FriendInfoDto> friendInfoDtos = new ArrayList<>();

        for (Relationship relationship : relationships) {
            User user = userRepository.findByEmail(
                    relationship.getUserEmail()
            ).orElse(null);
            if (user != null) {
                friendInfoDtos.add(
                        userToFriendInfoDto(user,relationship.getRelationshipStatus())
                );
            }

        }
        return friendInfoDtos;
    }

    private FriendInfoDto userToFriendInfoDto(User user,RelationshipStatus relationshipStatus){
        return FriendInfoDto.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .state(user.getState())
                .school(user.getSchool())
                .birth(user.getBirth())
                .relationshipStatus(relationshipStatus)
                .build();
    }

    public List getFriendInfoByUserEmailAndUsers(String userEmail, List<User> usersByState) {
        List<FriendInfoDto> friendInfoDtos = new ArrayList<>();
        for (User user : usersByState) {
            RelationshipStatus relationshipStatus = getRelationshipStatus(userEmail, user.getEmail());
            if (relationshipStatus.equals(RelationshipStatus.NONE)) {
                friendInfoDtos.add(
                        FriendInfoDto.builder()
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
        return friendInfoDtos;
    }
    //TODO validation check logic refactor
    private RelationshipStatus getRelationshipStatus(String userEmail, String friendEmail) {
        Relationship relationship =
                relationshipRepository.findByUserEmailAndRequestEmail(userEmail, friendEmail).orElse(null);
        if (relationship == null) {
            Relationship friendRelationship =
                    relationshipRepository.findByUserEmailAndRequestEmail(friendEmail, userEmail).orElse(null);
            if(friendRelationship == null){
                return RelationshipStatus.NONE;
            }
                return friendRelationship.getRelationshipStatus();
        }
        return relationship.getRelationshipStatus();
    }
}
