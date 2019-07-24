package com.ryulth.sns.friend.repository;

import com.ryulth.sns.friend.entity.Relationship;
import com.ryulth.sns.friend.entity.RelationshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface RelationshipRepository extends JpaRepository<Relationship,Long> {
    List<Relationship> findAllByUserEmail(String userEmail);
    List<Relationship> findAllByUserEmailAndRelationshipStatus(String userEmail, RelationshipStatus relationshipStatus);
    List<Relationship> findAllByRequestEmailAndRelationshipStatus(String requestEmail, RelationshipStatus relationshipStatus);
    Boolean existsByUserEmailAndRequestEmail(String userEmail,String requestEmail);
    Optional<Relationship> findByUserEmailAndRequestEmail(String userEmail,String requestEmail);

    @Transactional
    @Modifying
    @Query("delete from Relationship r where r.id=?1")
    void deleteById(Long id);

    @Transactional
    @Modifying
    @Query("delete from Relationship r where r.userEmail=?1 AND r.requestEmail=?2")
    void deleteByUserEmailAndRequestEmail(String userEmail,String requestEmail);
}
