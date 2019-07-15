package com.ryulth.timeline.account.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface TokenService {
    <T> Map<String,Object> publishToken(Map<String,Object> body, T subject);
    String getEmailFromToken(String token) throws IllegalAccessException;
}
