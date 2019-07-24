package com.ryulth.sns.account.exception;

public class EmailNotFoundException extends RuntimeException{

    public EmailNotFoundException() {
        super("Email not found");
    }

    public EmailNotFoundException(String message) {
        super(message);
    }

}