package com.ryulth.sns.account.service.user;

import com.ryulth.sns.account.dto.TokenDto;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface TokenService {
    <T> TokenDto publishToken(Map<String,Object> body, T subject);
    String getEmailFromRefreshToken(String token) throws IllegalAccessException;
    String getEmailFromAccessToken(String token) throws IllegalAccessException;
    boolean isUsable(String jwt) throws Exception;

}
