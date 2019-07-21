package com.ryulth.timeline.apis.repository;

import com.ryulth.timeline.apis.entity.Relationship;
import com.ryulth.timeline.apis.entity.RelationshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship,Long> {
    List<Relationship> findAllByUserEmail(String userEmail);
    List<Relationship> findAllByUserEmailAndRelationshipStatus(String userEmail, RelationshipStatus relationshipStatus);
    Boolean existsByUserEmailAndRequestEmail(String userEmail,String requestEmail);
    Optional<Relationship> findByUserEmailAndRequestEmail(String userEmail,String requestEmail);
}
