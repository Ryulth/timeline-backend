package com.ryulth.timeline.apis.service;

import com.ryulth.timeline.account.dto.UserInfoDto;
import com.ryulth.timeline.account.entity.User;
import com.ryulth.timeline.account.repository.UserRepository;
import com.ryulth.timeline.apis.dto.ApplyFriendDto;
import com.ryulth.timeline.apis.entity.Relationship;
import com.ryulth.timeline.apis.entity.RelationshipStatus;
import com.ryulth.timeline.apis.repository.RelationshipRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class RelationshipService {

    private final RelationshipRepository relationshipRepository;
    private final UserRepository userRepository;

    public RelationshipService(RelationshipRepository relationshipRepository, UserRepository userRepository) {
        this.relationshipRepository = relationshipRepository;
        this.userRepository = userRepository;
    }

    public Map<String, Object> applyRelationship(String userEmail, String applyEmail) {

        Relationship friendRelationship =
                relationshipRepository.findByUserEmailAndFriendEmail(applyEmail, userEmail)
                        .orElse(null);

        if (friendRelationship == null) {
            relationshipRepository.save(Relationship.builder()
                    .userEmail(userEmail)
                    .friendEmail(applyEmail)
                    .relationshipStatus(RelationshipStatus.APPLY)
                    .build());
            return Collections.singletonMap("apply", true);
        }

        if (friendRelationship.getRelationshipStatus().equals(RelationshipStatus.BLOCK)) {
            throw new RelationshipBlockException();
        }

        return Collections.singletonMap("apply", false);
    }

    public Map<String, Object> acceptRelationShip(String userEmail, String applyEmail) {
        Relationship friendRelationship =
                relationshipRepository.findByUserEmailAndFriendEmail(userEmail, applyEmail)
                        .orElseThrow(EntityNotFoundException::new);

        if (friendRelationship.getRelationshipStatus().equals(RelationshipStatus.APPLY)) {
            friendRelationship.setRelationshipStatus(RelationshipStatus.ACCEPT);
            relationshipRepository.save(friendRelationship);
            return Collections.singletonMap("accept", true);
        }
        return Collections.singletonMap("accept", false);
    }

    public Map<String, Object> recommendRelationship(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(EntityNotFoundException::new);

        String userState = user.getState();

        List<User> stateUsers = userRepository.findAllByState(userState);

        stateUsers.removeIf(u -> isRelationship(userEmail, u.getEmail()));

        List<UserInfoDto> userInfoDtos = new ArrayList<>();

        for (User u : stateUsers) {
            userInfoDtos.add(
                    UserInfoDto.builder()
                            .email(u.getEmail())
                            .username(u.getUsername())
                            .state(u.getState())
                            .school(u.getSchool())
                            .birth(u.getBirth())
                            .build()
            );
        }

        return Collections.singletonMap("users", userInfoDtos);
    }

    public Map<String, Object> getApplys(String userEmail) {
        return Collections.singletonMap("users", getRelationships(userEmail,RelationshipStatus.APPLY));
    }

    public Map<String, Object> getAccepts(String userEmail) {
        return Collections.singletonMap("users", getRelationships(userEmail,RelationshipStatus.ACCEPT));
    }

    private List<UserInfoDto> getRelationships(String userEmail,RelationshipStatus relationshipStatus){
        List<Relationship> relationships =
                relationshipRepository.findAllByUserEmailAndRelationshipStatus(userEmail, relationshipStatus);

        List<UserInfoDto> userInfoDtos = new ArrayList<>();

        for (Relationship relationship : relationships) {
            User user = userRepository.findByEmail(
                    relationship.getFriendEmail()
            ).orElse(null);
            if (user != null) {
                userInfoDtos.add(
                        UserInfoDto.builder()
                                .email(user.getEmail())
                                .username(user.getUsername())
                                .state(user.getState())
                                .school(user.getSchool())
                                .birth(user.getBirth())
                                .build()
                );
            }

        }
        return userInfoDtos;
    };
    private boolean isRelationship(String userEmail, String friendEmail) {
        Relationship relationship =
                relationshipRepository.findByUserEmailAndFriendEmail(userEmail, friendEmail).orElse(null);
        if (relationship == null) {
            return false;
        }
        if (relationship.getRelationshipStatus().equals(RelationshipStatus.ACCEPT)) {
            return true;
        }
        return false;
    }
}
