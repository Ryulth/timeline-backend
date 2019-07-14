package com.ryulth.timeline.account.controller;

import com.ryulth.timeline.account.dto.RegisterDto;
import com.ryulth.timeline.account.service.AccountService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        this.accountService = accountService;
    }

    @PostMapping("apis/accounts/register")
    @ApiOperation(value="Register API", notes="회원가입을 하면 토큰을 반환하는 API. (Authorization Header 필요 없습니다. Swagger 전역 설정 원인)")
    public ResponseEntity register(
            @RequestBody RegisterDto registerDto) {
        try {
            return new ResponseEntity<>(accountService.register(registerDto), httpHeaders, HttpStatus.OK);
        } catch (EntityExistsException e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), httpHeaders, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            log.error(e.toString());
            return new ResponseEntity<>(Collections.singletonMap("error","INTERNAL SERVER ERROR"),httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("apis/accounts/duplicate/{email}")
    @ApiOperation(value="Duplicate API", notes="회원가입시 회원 아이디(email) 중복 체크하는 API. (Authorization Header 필요 없습니다. Swagger 전역 설정 원인)")
    public ResponseEntity duplicate(
            @PathVariable("email") String email) {
        try {
            if (accountService.duplicateEmail(email)){
                return new ResponseEntity<>(Collections.singletonMap("duplicate", true), httpHeaders, HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(Collections.singletonMap("duplicate", false), httpHeaders, HttpStatus.OK);
            }
        } catch (Exception e) {
            log.error(e.toString());
            return new ResponseEntity<>(Collections.singletonMap("error","INTERNAL SERVER ERROR"),httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
