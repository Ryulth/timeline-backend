package com.ryulth.timeline.apis.controller;


import com.ryulth.timeline.apis.dto.ApplyFriendDto;
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

    @PostMapping("friends/apply")
    @ApiOperation(value = "Friend apply API", notes = "친구 신청")
    public ResponseEntity recommendFriends(
            HttpServletRequest httpServletRequest,
            @RequestBody ApplyFriendDto applyFriendDto
    ) {
        try {
            String email = httpServletRequest.getSession().getAttribute("email").toString();
            return new ResponseEntity<>(relationshipService.applyRelationship(email, applyFriendDto.getApplyEmail()), httpHeaders, HttpStatus.OK);
        } catch (RelationshipBlockException e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), httpHeaders, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            log.error(e.toString());
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("friends/apply")
    @ApiOperation(value = "Friend apply list API", notes = "신청 들어온 목록")
    public ResponseEntity getApplyFriends(
            HttpServletRequest httpServletRequest
    ) {
        try {
            String email = httpServletRequest.getSession().getAttribute("email").toString();
            return new ResponseEntity<>(relationshipService.getApplys(email), httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.toString());
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("friends/accept")
    @ApiOperation(value = "Friend accept API", notes = "친구 수락")
    public ResponseEntity acceptFriends(
            HttpServletRequest httpServletRequest,
            @RequestBody ApplyFriendDto applyFriendDto
    ) {
        try {
            String email = httpServletRequest.getSession().getAttribute("email").toString();
            return new ResponseEntity<>(relationshipService.acceptRelationShip(email, applyFriendDto.getApplyEmail()), httpHeaders, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), httpHeaders, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            log.error(e.toString());
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping("friends/accept")
    @ApiOperation(value = "Friend accept API", notes = "수락한 친구 목록 == 친구목록")
    public ResponseEntity getAcceptFriends(
            HttpServletRequest httpServletRequest
    ) {
        try {
            String email = httpServletRequest.getSession().getAttribute("email").toString();
            return new ResponseEntity<>(relationshipService.getAccepts(email), httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.toString());
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
