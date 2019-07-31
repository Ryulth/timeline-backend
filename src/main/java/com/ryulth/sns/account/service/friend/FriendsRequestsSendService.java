package com.ryulth.sns.account.service.friend;

import com.ryulth.sns.account.dto.FriendsDto;
import com.ryulth.sns.account.dto.SuccessDto;
import com.ryulth.sns.account.entity.Relationship;
import com.ryulth.sns.account.entity.RelationshipStatus;
import com.ryulth.sns.account.repository.RelationshipRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendsRequestsSendService {
    private final RelationshipRepository relationshipRepository;
    private final RelationshipService relationshipService;

    public FriendsRequestsSendService(RelationshipRepository relationshipRepository, RelationshipService relationshipService) {
        this.relationshipRepository = relationshipRepository;
        this.relationshipService = relationshipService;
    }

    public FriendsDto getFriendsRequestsSends(String userEmail) {
        List<Relationship> relationships =
                relationshipRepository.findAllByUserEmailAndRelationshipStatus(userEmail, RelationshipStatus.REQUEST);

        return relationshipService.getSendFriendInfoByRelationships(relationships);
    }

    public SuccessDto deleteFriendsRequestsSend(String userEmail, String requestEmail) {
        relationshipRepository.deleteByUserEmailAndRequestEmail(userEmail, requestEmail);
        return SuccessDto.builder().success(true).build();
    }

    @CacheEvict(value = "friends.recommend" , key = "#userEmail")
    public SuccessDto makeFriendsRequestsSend(String userEmail, String requestEmail) {
        if (userEmail.equals(requestEmail)) {
            return SuccessDto.builder().success(false).build();
        }
        if (relationshipRepository.existsByUserEmailAndRequestEmail(userEmail, requestEmail)) {
            return SuccessDto.builder().success(false).build();
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
        return SuccessDto.builder().success(true).build();
    }
}
