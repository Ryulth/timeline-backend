package com.ryulth.sns.account.service.friend;

import com.ryulth.sns.account.dto.FriendsDto;
import com.ryulth.sns.account.dto.SuccessDto;
import com.ryulth.sns.account.entity.Relationship;
import com.ryulth.sns.account.entity.RelationshipStatus;
import com.ryulth.sns.account.entity.User;
import com.ryulth.sns.account.repository.RelationshipRepository;
import com.ryulth.sns.account.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

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

    @Cacheable(value = "friends.recommend" , key = "#userEmail")
    public FriendsDto recommendFriends(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(EntityNotFoundException::new);

        String userState = user.getState();

        List<User> usersByState = userRepository.findAllByState(userState);
        usersByState.removeIf(u -> relationshipRepository.existsByUserEmailAndRequestEmail(u.getEmail(), userEmail));
        usersByState.remove(user);

        return relationshipService.getFriendInfoByUserEmailAndUsers(userEmail, usersByState);
    }

    @Cacheable(value = "friends", key = "#userEmail")
    public FriendsDto getFriends(String userEmail) {
        List<Relationship> relationships =
                relationshipRepository.findAllByRequestEmailAndRelationshipStatus(userEmail, RelationshipStatus.FRIEND);

        return relationshipService.getReceiveFriendInfoByRelationships(relationships);
    }

    @Transactional
    @CacheEvict(value = "friends", key = "#userEmail")
    public SuccessDto deleteFriend(String userEmail, String requestEmail) {
        try {
            relationshipRepository.deleteByUserEmailAndRequestEmail(userEmail, requestEmail);
            relationshipRepository.deleteByUserEmailAndRequestEmail(requestEmail, userEmail);
            return SuccessDto.builder().success(true).build();
        } catch (Exception e) {
            throw e;
        }
    }
}
