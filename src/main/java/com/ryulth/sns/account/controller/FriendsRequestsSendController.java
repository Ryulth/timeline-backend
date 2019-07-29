package com.ryulth.sns.account.controller;

import com.ryulth.sns.account.dto.FriendRequestDto;
import com.ryulth.sns.account.service.friend.FriendsRequestsSendService;
import com.ryulth.sns.account.service.friend.RelationshipBlockException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Slf4j
@RestController
@Api(value = "FriendsRequestsSendController")
public class FriendsRequestsSendController {


    private static final HttpHeaders httpHeaders = new HttpHeaders();
    private final FriendsRequestsSendService friendsRequestsSendService;

    public FriendsRequestsSendController(FriendsRequestsSendService friendsRequestsSendService) {
        this.friendsRequestsSendService = friendsRequestsSendService;
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    }

    @PostMapping("friends/requests/send")
    @ApiOperation(value = "Friend request send API", notes = "친구 신청")
    public ResponseEntity requestFriends(
            HttpServletRequest httpServletRequest,
            @RequestBody FriendRequestDto friendRequestDto
    ) {
        try {
            String email = httpServletRequest.getSession().getAttribute("email").toString();
            return new ResponseEntity<>(friendsRequestsSendService.makeFriendsRequestsSend(email, friendRequestDto.getRequestEmail()), httpHeaders, HttpStatus.OK);
        } catch (RelationshipBlockException e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), httpHeaders, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("friends/requests/sends")
    @ApiOperation(value = "Friend apply list API", notes = "신청 한 목록")
    public ResponseEntity getFriendsRequests(
            HttpServletRequest httpServletRequest
    ) {
        try {
            String email = httpServletRequest.getSession().getAttribute("email").toString();
            return new ResponseEntity<>(friendsRequestsSendService.getFriendsRequestsSends(email), httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("friends/requests/sends/{requestEmail:.+}}")
    @ApiOperation(value = "Friend request send API", notes = "친구 신청")
    public ResponseEntity requestFriends(
            HttpServletRequest httpServletRequest,
            @PathVariable("requestEmail") String requestEmail
    ) {
        try {
            String email = httpServletRequest.getSession().getAttribute("email").toString();

            return new ResponseEntity<>(friendsRequestsSendService.deleteFriendsRequestsSend(email, requestEmail), httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
