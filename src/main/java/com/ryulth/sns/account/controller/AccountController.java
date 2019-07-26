package com.ryulth.sns.account.controller;

import com.ryulth.sns.account.dto.LoginDto;
import com.ryulth.sns.account.dto.RegisterDto;
import com.ryulth.sns.account.exception.EmailNotFoundException;
import com.ryulth.sns.account.service.user.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import java.util.Collections;

@Slf4j
@RestController
@Api(value = "AccountController")
public class AccountController {
    private static final HttpHeaders httpHeaders = new HttpHeaders();
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        this.accountService = accountService;
    }

    @PostMapping("accounts/register")
    @ApiOperation(value = "Register API", notes = "회원가입을 하면 토큰을 반환하는 API. (Authorization Header 필요 없습니다. Swagger 전역 설정 원인)")
    public ResponseEntity register(
            @RequestBody RegisterDto registerDto) {
        try {
            return new ResponseEntity<>(accountService.register(registerDto), httpHeaders, HttpStatus.OK);
        } catch (EntityExistsException e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), httpHeaders, HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error(e.toString());
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("accounts/login")
    @ApiOperation(value = "Login API", notes = "로그인을 하면 토큰을 반환하는 API. (Authorization Header 필요 없습니다. Swagger 전역 설정 원인)")
    public ResponseEntity login(
            @RequestBody LoginDto loginDto) {
        try {
            return new ResponseEntity<>(accountService.login(loginDto), httpHeaders, HttpStatus.OK);
        } catch (EmailNotFoundException | BadCredentialsException e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), httpHeaders, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            log.error(e.toString());
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("accounts/duplicate/{email}")
    @ApiOperation(value = "Duplicate API", notes = "회원가입시 회원 아이디(email) 중복 체크하는 API. (Authorization Header 필요 없습니다. Swagger 전역 설정 원인)")
    public ResponseEntity duplicate(
            @PathVariable("email") String email) {
        try {
            if (accountService.duplicateEmail(email)) {
                return new ResponseEntity<>(Collections.singletonMap("duplicate", true), httpHeaders, HttpStatus.CONFLICT);
            } else {
                return new ResponseEntity<>(Collections.singletonMap("duplicate", false), httpHeaders, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(e.toString());
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("accounts/refresh/{token}")
    @ApiOperation(value = "Update Access Token API", notes = "refresh_token 을 보내면 access_token 을 반환해주는 API. (Authorization Header 필요 없습니다. Swagger 전역 설정 원인)")
    public ResponseEntity updateAccessToken(
            @PathVariable("token") String refreshToken) {
        try {
            return new ResponseEntity<>(accountService.updateAccessToken(refreshToken), httpHeaders, HttpStatus.OK);
        } catch (IllegalAccessException | EmailNotFoundException e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), httpHeaders, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            log.error(e.toString());
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
