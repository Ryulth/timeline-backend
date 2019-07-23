package com.ryulth.timeline.friend.service;

public class RelationshipBlockException extends RuntimeException {
    public RelationshipBlockException() {
        super("Block Error");
    }

    public RelationshipBlockException(String message) {
        super(message);
    }
}
