package com.ryulth.timeline.apis.service;

public class RelationshipBlockException extends RuntimeException {
    public RelationshipBlockException() {
        super("Block Error");
    }

    public RelationshipBlockException(String message) {
        super(message);
    }
}
