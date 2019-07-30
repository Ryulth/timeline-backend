package com.ryulth.sns.account.controller;

import com.ryulth.sns.account.dto.FriendAcceptDto;
import com.ryulth.sns.account.service.friend.FriendsRequestsReceiveService;
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
@Api(value = "FriendsRequestsReceiveController")
public class FriendsRequestsReceiveController {

    private static final HttpHeaders httpHeaders = new HttpHeaders();
    private final FriendsRequestsReceiveService friendsRequestsReceiveService;

    public FriendsRequestsReceiveController(FriendsRequestsReceiveService friendsRequestsReceiveService) {
        this.friendsRequestsReceiveService = friendsRequestsReceiveService;
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    }


    @GetMapping("friends/requests/receive")
    @ApiOperation(value = "Friend apply list API", notes = "신청 들어온 목록")
    public ResponseEntity getFriendsRequests(
            HttpServletRequest httpServletRequest
    ) {
        try {
            String email = httpServletRequest.getSession().getAttribute("email").toString();
            return new ResponseEntity<>(friendsRequestsReceiveService.getFriendsRequestsReceives(email), httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("friends/requests/receives/{requestEmail:.+}")
    @ApiOperation(value = "Friend accept API", notes = "친구 수락 / 거절")
    public ResponseEntity acceptFriends(
            HttpServletRequest httpServletRequest,
            @PathVariable("requestEmail") String requestEmail,
            @RequestBody FriendAcceptDto friendAcceptDto
    ) {
        try {
            String email = httpServletRequest.getSession().getAttribute("email").toString();
            return new ResponseEntity<>(friendsRequestsReceiveService.acceptFriendsRequestsReceive(email,requestEmail,friendAcceptDto), httpHeaders, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), httpHeaders, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
