package com.ryulth.sns.account.entity;

import lombok.Getter;

@Getter
public enum  RelationshipStatus {
    NONE,
    REQUEST,
    FRIEND,
    BLOCK,
    REFUSE
}
