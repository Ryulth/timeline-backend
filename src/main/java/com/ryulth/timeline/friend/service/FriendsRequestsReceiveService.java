package com.ryulth.timeline.friend.service;

import com.ryulth.timeline.friend.dto.FriendAcceptDto;
import com.ryulth.timeline.friend.entity.Relationship;
import com.ryulth.timeline.friend.entity.RelationshipStatus;
import com.ryulth.timeline.friend.repository.RelationshipRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class FriendsRequestsReceiveService {
    private final RelationshipService relationshipService;
    private final RelationshipRepository relationshipRepository;

    public FriendsRequestsReceiveService(RelationshipService relationshipService, RelationshipRepository relationshipRepository) {
        this.relationshipService = relationshipService;
        this.relationshipRepository = relationshipRepository;
    }

    public Map<String, Object> getFriendsRequestsReceives(String userEmail) {
        List<Relationship> relationships =
                relationshipRepository.findAllByRequestEmailAndRelationshipStatus(userEmail, RelationshipStatus.REQUEST);
        return Collections.singletonMap("users",
                relationshipService.getReceiveFriendInfoByRelationships(relationships));
    }

    public Map<String, Object> editFriendsRequestsReceive(String userEmail, String requestEmail, FriendAcceptDto friendAcceptDto) {
        Relationship relationship =
                relationshipRepository.findByUserEmailAndRequestEmail(requestEmail, userEmail)
                        .orElseThrow(EntityNotFoundException::new);

        if (!friendAcceptDto.isAccept()) {
            relationship.setRelationshipStatus(RelationshipStatus.REFUSE);
            relationshipRepository.save(relationship);
            return Collections.singletonMap("accept", false);
        }

        if (relationship.getRelationshipStatus().equals(RelationshipStatus.REQUEST)) {
            relationship.setRelationshipStatus(RelationshipStatus.FRIEND);
            relationshipRepository.save(relationship);
            saveRequestUserRelationship(requestEmail, userEmail);
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
}

