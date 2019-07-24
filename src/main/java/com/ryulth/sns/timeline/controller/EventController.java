package com.ryulth.sns.timeline.controller;


import com.ryulth.sns.timeline.dto.NewEventDto;
import com.ryulth.sns.timeline.security.UnauthorizedException;
import com.ryulth.sns.timeline.service.EventService;
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
@Api(value = "EventController")
public class EventController {
    private static final HttpHeaders httpHeaders = new HttpHeaders();
    private final EventService eventService;

    public EventController(EventService eventService) {
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        this.eventService = eventService;
    }

    @PostMapping("event")
    @ApiOperation(value = "New Event API", notes = "이벤트 작성 API")
    public ResponseEntity newEvent(
            HttpServletRequest httpServletRequest,
            @RequestBody NewEventDto newEventDto
    ) {
        try {
            String email = httpServletRequest.getSession().getAttribute("email").toString();
            return new ResponseEntity<>(eventService.registerEvent(newEventDto, email), httpHeaders, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("events/{eventId}")
    @ApiOperation(value = "Get Event API", notes = "이벤트 ID 로 가져온다.")
    public ResponseEntity newEvent(
            HttpServletRequest httpServletRequest,
            @PathVariable("eventId") Long eventId
    ) {
        try {
            String email = httpServletRequest.getSession().getAttribute("email").toString();
            return new ResponseEntity<>(eventService.getEventById(eventId, email), httpHeaders, HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), httpHeaders, HttpStatus.UNAUTHORIZED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), httpHeaders, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
