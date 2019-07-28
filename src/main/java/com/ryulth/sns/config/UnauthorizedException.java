package com.ryulth.sns.config;

public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException() {
        super("Unauthorized Error");
    }

    public UnauthorizedException(String message) {
        super(message);
    }

}
