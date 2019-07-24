package com.ryulth.sns.friend.service;

import com.ryulth.sns.friend.entity.Relationship;
import com.ryulth.sns.friend.entity.RelationshipStatus;
import com.ryulth.sns.friend.repository.RelationshipRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class FriendsRequestsSendService {
    private final RelationshipRepository relationshipRepository;
    private final RelationshipService relationshipService;

    public FriendsRequestsSendService(RelationshipRepository relationshipRepository, RelationshipService relationshipService) {
        this.relationshipRepository = relationshipRepository;
        this.relationshipService = relationshipService;
    }

    public Map<String, Object> getFriendsRequestsSends(String userEmail) {
        List<Relationship> relationships =
                relationshipRepository.findAllByUserEmailAndRelationshipStatus(userEmail, RelationshipStatus.REQUEST);

        return Collections.singletonMap("users",
                relationshipService.getSendFriendInfoByRelationships(relationships));
    }

    public Map<String, Object> deleteFriendsRequestsSend(String userEmail, String requestEmail) {
        relationshipRepository.deleteByUserEmailAndRequestEmail(userEmail, requestEmail);
        return Collections.singletonMap("delete", true);
    }

    public Map<String, Object> makeFriendsRequestsSend(String userEmail, String requestEmail) {
        if (userEmail.equals(requestEmail)) {
            return Collections.singletonMap("request", false);
        }
        if (relationshipRepository.existsByUserEmailAndRequestEmail(userEmail, requestEmail)) {
            return Collections.singletonMap("request", false);
        }
        Relationship friendRelationship =
                relationshipRepository.findByUserEmailAndRequestEmail(requestEmail, userEmail)
                        .orElse(Relationship.builder()
                                .userEmail(userEmail)
                                .requestEmail(requestEmail)
                                .relationshipStatus(RelationshipStatus.NONE)
                                .build());

        if (friendRelationship.getRelationshipStatus().equals(RelationshipStatus.BLOCK)) {
            throw new RelationshipBlockException();
        }

        relationshipRepository.save(Relationship.builder()
                .userEmail(userEmail)
                .requestEmail(requestEmail)
                .relationshipStatus(RelationshipStatus.REQUEST)
                .build());
        return Collections.singletonMap("request", true);
    }
}
