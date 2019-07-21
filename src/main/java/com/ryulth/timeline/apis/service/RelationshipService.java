package com.ryulth.timeline.apis.service;

import com.ryulth.timeline.account.entity.User;
import com.ryulth.timeline.account.repository.UserRepository;
import com.ryulth.timeline.apis.dto.FriendAcceptDto;
import com.ryulth.timeline.apis.dto.FriendInfoDto;
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

    public Map<String, Object> requestRelationship(String userEmail, String requestEmail) {
        if (userEmail.equals(requestEmail)) {
            return Collections.singletonMap("request", false);
        }
        Relationship friendRelationship =
                relationshipRepository.findByUserEmailAndRequestEmail(requestEmail, userEmail)
                        .orElse(null);

        if (friendRelationship == null) {
            relationshipRepository.save(Relationship.builder()
                    .userEmail(userEmail)
                    .requestEmail(requestEmail)
                    .relationshipStatus(RelationshipStatus.REQUEST)
                    .build());
            return Collections.singletonMap("request", true);
        }

        if (friendRelationship.getRelationshipStatus().equals(RelationshipStatus.BLOCK)) {
            throw new RelationshipBlockException();
        }

        return Collections.singletonMap("request", false);
    }

    public Map<String, Object> acceptRelationShip(String userEmail, String requestEmail, FriendAcceptDto friendAcceptDto) {
        Relationship relationship =
                relationshipRepository.findByUserEmailAndRequestEmail(userEmail, requestEmail)
                        .orElseThrow(EntityNotFoundException::new);

        if (!friendAcceptDto.isAccept()) {
            relationshipRepository.deleteById(relationship.getId());
            return Collections.singletonMap("accept", false);
        }


        if (relationship.getRelationshipStatus().equals(RelationshipStatus.REQUEST)) {
            relationship.setRelationshipStatus(RelationshipStatus.FRIEND);
            relationshipRepository.save(relationship);
            saveRequestUserRelationship(userEmail,requestEmail);
            return Collections.singletonMap("accept", true);
        }
        return Collections.singletonMap("accept", false);
    }

    private void saveRequestUserRelationship(String userEmail, String requestEmail) {
        Relationship relationship = relationshipRepository.findByUserEmailAndRequestEmail(
                requestEmail, userEmail)
                .orElse(Relationship.builder()
                        .userEmail(requestEmail)
                        .requestEmail(userEmail)
                        .relationshipStatus(RelationshipStatus.FRIEND)
                        .build());
        relationship.setRelationshipStatus(RelationshipStatus.FRIEND);
        relationshipRepository.save(relationship);
    }

    public Map<String, Object> recommendRelationship(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(EntityNotFoundException::new);

        String userState = user.getState();

        List<User> usersByState = userRepository.findAllByState(userState);
        usersByState.removeIf(u -> relationshipRepository.existsByUserEmailAndRequestEmail(u.getEmail(), userEmail));
        usersByState.remove(user);

        return Collections.singletonMap("users", getFriendRelationships(userEmail, usersByState));
    }

    private List getFriendRelationships(String userEmail, List<User> usersByState) {
        List<FriendInfoDto> friendInfoDtos = new ArrayList<>();
        for (User user : usersByState) {
            RelationshipStatus relationshipStatus = getRelationshipStatus(userEmail, user.getEmail());
            if (!relationshipStatus.equals(RelationshipStatus.FRIEND)) {
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

    private RelationshipStatus getRelationshipStatus(String userEmail, String friendEmail) {
        Relationship relationship =
                relationshipRepository.findByUserEmailAndRequestEmail(userEmail, friendEmail).orElse(null);
        if (relationship == null) {
            return RelationshipStatus.NONE;
        }
        return relationship.getRelationshipStatus();
    }

    public Map<String, Object> getRequests(String userEmail) {
        return Collections.singletonMap("users", getRelationships(userEmail, RelationshipStatus.REQUEST));
    }

    public Map<String, Object> getFriends(String userEmail) {
        return Collections.singletonMap("users", getRelationships(userEmail, RelationshipStatus.FRIEND));
    }

    private List getRelationships(String userEmail, RelationshipStatus relationshipStatus) {
        List<Relationship> relationships =
                relationshipRepository.findAllByUserEmailAndRelationshipStatus(userEmail, relationshipStatus);

        List<FriendInfoDto> friendInfoDtos = new ArrayList<>();

        for (Relationship relationship : relationships) {
            User user = userRepository.findByEmail(
                    relationship.getRequestEmail()
            ).orElse(null);
            if (user != null) {
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


}
