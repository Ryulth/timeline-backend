package com.ryulth.sns.friend.service;

import com.ryulth.sns.account.entity.User;
import com.ryulth.sns.account.repository.UserRepository;
import com.ryulth.sns.friend.entity.Relationship;
import com.ryulth.sns.friend.entity.RelationshipStatus;
import com.ryulth.sns.friend.repository.RelationshipRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class FriendService {
    private final UserRepository userRepository;
    private final RelationshipRepository relationshipRepository;
    private final RelationshipService relationshipService;

    public FriendService(UserRepository userRepository, RelationshipRepository relationshipRepository, RelationshipService relationshipService) {
        this.userRepository = userRepository;
        this.relationshipRepository = relationshipRepository;
        this.relationshipService = relationshipService;
    }

    public Map<String, Object> recommendFriends(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(EntityNotFoundException::new);

        String userState = user.getState();

        List<User> usersByState = userRepository.findAllByState(userState);
        usersByState.removeIf(u -> relationshipRepository.existsByUserEmailAndRequestEmail(u.getEmail(), userEmail));
        usersByState.remove(user);

        return Collections.singletonMap("users", relationshipService.getFriendInfoByUserEmailAndUsers(userEmail, usersByState));
    }

    public Map<String, Object> getFriends(String userEmail) {
        List<Relationship> relationships =
                relationshipRepository.findAllByRequestEmailAndRelationshipStatus(userEmail, RelationshipStatus.FRIEND);
        return Collections.singletonMap("users", relationshipService.getReceiveFriendInfoByRelationships(relationships));
    }
}
