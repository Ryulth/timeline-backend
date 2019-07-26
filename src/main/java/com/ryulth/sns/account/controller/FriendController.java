package com.ryulth.sns.account.controller;

import com.ryulth.sns.account.service.friend.FriendService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Slf4j
@RestController
@Api(value = "FriendController")
public class FriendController {
    private static final HttpHeaders httpHeaders = new HttpHeaders();
    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    }

    @GetMapping("friends/recommend")
    @ApiOperation(value = "Friend Recommend API", notes = "추천 친구를 받아온다")
    public ResponseEntity recommendFriends(
            HttpServletRequest httpServletRequest
    ) {
        try {
            String accessEmail = httpServletRequest.getSession().getAttribute("email").toString();
            return new ResponseEntity<>(friendService.recommendFriends(accessEmail), httpHeaders, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), httpHeaders, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("friends")
    @ApiOperation(value = "Friend accept API", notes = "친구목록")
    public ResponseEntity getFriends(
            HttpServletRequest httpServletRequest
    ) {
        try {
            String accessEmail = httpServletRequest.getSession().getAttribute("email").toString();
            return new ResponseEntity<>(friendService.getFriends(accessEmail), httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("friends/{requestEmail}")
    @ApiOperation(value = "Friend delete API", notes = "친구 삭제")
    public ResponseEntity deleteFriends(
            HttpServletRequest httpServletRequest,
            @PathVariable("requestEmail") String requestEmail
    ) {
        try {
            String accessEmail = httpServletRequest.getSession().getAttribute("email").toString();
            return new ResponseEntity<>(friendService.deleteFriend(accessEmail, requestEmail), httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}