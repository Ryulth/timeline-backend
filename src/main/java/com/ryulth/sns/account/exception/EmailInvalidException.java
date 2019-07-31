package com.ryulth.sns.account.exception;

public class EmailInvalidException extends RuntimeException{

    public EmailInvalidException() {
        super("Email Invalid");
    }

    public EmailInvalidException(String message) {
        super(message);
    }

}