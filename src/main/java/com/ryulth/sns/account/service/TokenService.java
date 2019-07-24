package com.ryulth.sns.account.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface TokenService {
    <T> Map<String,Object> publishToken(Map<String,Object> body, T subject);
    String getEmailFromRefreshToken(String token) throws IllegalAccessException;
    String getEmailFromAccessToken(String token) throws IllegalAccessException;
    boolean isUsable(String jwt) throws Exception;

}
