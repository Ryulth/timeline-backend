package com.ryulth.sns.account.service.friend;

import com.ryulth.sns.account.dto.FriendAcceptDto;
import com.ryulth.sns.account.dto.FriendsDto;
import com.ryulth.sns.account.dto.SuccessDto;
import com.ryulth.sns.account.entity.Relationship;
import com.ryulth.sns.account.entity.RelationshipStatus;
import com.ryulth.sns.account.repository.RelationshipRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class FriendsRequestsReceiveService {
    private final RelationshipService relationshipService;
    private final RelationshipRepository relationshipRepository;

    public FriendsRequestsReceiveService(RelationshipService relationshipService, RelationshipRepository relationshipRepository) {
        this.relationshipService = relationshipService;
        this.relationshipRepository = relationshipRepository;
    }

    public FriendsDto getFriendsRequestsReceives(String userEmail) {
        List<Relationship> relationships =
                relationshipRepository.findAllByRequestEmailAndRelationshipStatus(userEmail, RelationshipStatus.REQUEST);
        return relationshipService.getReceiveFriendInfoByRelationships(relationships);
    }


    public SuccessDto acceptFriendsRequestsReceive(String userEmail, String requestEmail, FriendAcceptDto friendAcceptDto) {
        Relationship relationship =
                relationshipRepository.findByUserEmailAndRequestEmail(requestEmail, userEmail)
                        .orElseThrow(EntityNotFoundException::new);

        if (!friendAcceptDto.isAccept()) {
            relationshipService.refuseRelationshipStatus(relationship);
            return SuccessDto.builder().success(false).build();
        }

        if (relationship.getRelationshipStatus().equals(RelationshipStatus.REQUEST)) {
            relationshipService.friendRelationshipStatus(relationship);
            saveRequestUserRelationship(requestEmail, userEmail);
            return SuccessDto.builder().success(true).build();
        }
        return SuccessDto.builder().success(false).build();
    }

    private void saveRequestUserRelationship(String userEmail, String requestEmail) {
        Relationship relationship = relationshipRepository.findByUserEmailAndRequestEmail(
                requestEmail, userEmail)
                .orElse(Relationship.builder()
                        .userEmail(requestEmail)
                        .requestEmail(userEmail)
                        .relationshipStatus(RelationshipStatus.FRIEND)
                        .build());
        relationshipService.friendRelationshipStatus(relationship);
    }
}

