package com.ryulth.timeline.apis.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "relationship")
@ToString
public class Relationship {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "relationship_id")
    private Long id;

    @Column(name = "relationship_user_email")
    private String userEmail;
    @Column(name = "relationship_friend_email")
    private String friendEmail;

    @Column(name = "relationship_status")
    @Enumerated(EnumType.STRING)
    private RelationshipStatus relationshipStatus;

    public void setRelationshipStatus(RelationshipStatus relationshipStatus) {
        this.relationshipStatus = relationshipStatus;
    }
}
