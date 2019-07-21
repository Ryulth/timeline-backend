package com.ryulth.timeline.apis.controller;


import com.ryulth.timeline.apis.dto.FriendAcceptDto;
import com.ryulth.timeline.apis.dto.FriendRequestDto;
import com.ryulth.timeline.apis.service.RelationshipBlockException;
import com.ryulth.timeline.apis.service.RelationshipService;
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
@Api(value = "RelationshipController")
public class RelationshipController {
    private static final HttpHeaders httpHeaders = new HttpHeaders();
    private final RelationshipService relationshipService;

    public RelationshipController(RelationshipService relationshipService) {
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        this.relationshipService = relationshipService;
    }

    @GetMapping("friends/recommend")
    @ApiOperation(value = "Friend Recommend API", notes = "추천 친구를 받아온다")
    public ResponseEntity recommendFriends(
            HttpServletRequest httpServletRequest
    ) {
        try {
            String email = httpServletRequest.getSession().getAttribute("email").toString();
            return new ResponseEntity<>(relationshipService.recommendRelationship(email), httpHeaders, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), httpHeaders, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            log.error(e.toString());
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("friends/request")
    @ApiOperation(value = "Friend apply API", notes = "친구 신청")
    public ResponseEntity requestFriends(
            HttpServletRequest httpServletRequest,
            @RequestBody FriendRequestDto friendRequestDto
    ) {
        try {
            String email = httpServletRequest.getSession().getAttribute("email").toString();
            return new ResponseEntity<>(relationshipService.requestRelationship(email, friendRequestDto.getRequestEmail()), httpHeaders, HttpStatus.OK);
        } catch (RelationshipBlockException e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), httpHeaders, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            log.error(e.toString());
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("friends/requests")
    @ApiOperation(value = "Friend apply list API", notes = "신청 들어온 목록")
    public ResponseEntity getFriendsRequests(
            HttpServletRequest httpServletRequest
    ) {
        try {
            String email = httpServletRequest.getSession().getAttribute("email").toString();
            return new ResponseEntity<>(relationshipService.getRequests(email), httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.toString());
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("friends/requests/{requestEmail}")
    @ApiOperation(value = "Friend accept API", notes = "친구 수락 / 거절")
    public ResponseEntity acceptFriends(
            HttpServletRequest httpServletRequest,
            @PathVariable("requestEmail") String requestEmail,
            @RequestBody FriendAcceptDto friendAcceptDto
            ) {
        try {
            String email = httpServletRequest.getSession().getAttribute("email").toString();
            return new ResponseEntity<>(relationshipService.acceptRelationShip(email,requestEmail,friendAcceptDto), httpHeaders, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), httpHeaders, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            log.error(e.toString());
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("friends")
    @ApiOperation(value = "Friend accept API", notes = "친구목록")
    public ResponseEntity getFriends(
            HttpServletRequest httpServletRequest
    ) {
        try {
            String email = httpServletRequest.getSession().getAttribute("email").toString();
            return new ResponseEntity<>(relationshipService.getFriends(email), httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
