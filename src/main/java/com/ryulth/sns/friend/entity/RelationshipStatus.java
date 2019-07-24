package com.ryulth.sns.friend.entity;

import lombok.Getter;

@Getter
public enum  RelationshipStatus {
    NONE,
    REQUEST,
    FRIEND,
    BLOCK,
    REFUSE
}
