package com.ryulth.sns.account.service.friend;

public class RelationshipBlockException extends RuntimeException {
    public RelationshipBlockException() {
        super("Block Error");
    }

    public RelationshipBlockException(String message) {
        super(message);
    }
}
