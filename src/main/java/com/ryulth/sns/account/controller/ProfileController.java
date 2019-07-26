package com.ryulth.sns.account.controller;


import com.ryulth.sns.account.dto.UserEditDto;
import com.ryulth.sns.account.service.user.ProfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Slf4j
@RestController
@Api(value = "ProfileController")
public class ProfileController {
    private static final HttpHeaders httpHeaders = new HttpHeaders();
    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    }

    @GetMapping("profile")
    @ApiOperation(value = "User Profile API", notes = "자기 자신 정보 받아옴")
    public ResponseEntity AccessToken(
            HttpServletRequest httpServletRequest) {
        try {
            String accessEmail = httpServletRequest.getSession().getAttribute("email").toString();
            return new ResponseEntity<>(profileService.getProfile(accessEmail), httpHeaders, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(Collections.singletonMap("error", "EMAIL NOT FOUND"), httpHeaders, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("profile")
    @ApiOperation(value = "User Profile API", notes = "자기 자신 정보 받아옴")
    public ResponseEntity AccessToken(
            HttpServletRequest httpServletRequest,
            @RequestBody UserEditDto userEditDto
    ) {
        try {
            String accessEmail = httpServletRequest.getSession().getAttribute("email").toString();
            return new ResponseEntity<>(profileService.editProfile(accessEmail, userEditDto), httpHeaders, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(Collections.singletonMap("error", "EMAIL NOT FOUND"), httpHeaders, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
