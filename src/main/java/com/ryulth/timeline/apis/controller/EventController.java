package com.ryulth.timeline.apis.controller;


import com.ryulth.timeline.apis.dto.NewEventDto;
import com.ryulth.timeline.apis.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("apis/event")
    @ApiOperation(value = "New Event API", notes = "이벤트 작성 API")
    public ResponseEntity newEvent(
            HttpServletRequest httpServletRequest,
            @RequestBody NewEventDto newEventDto
    ) {
        try {
            String email = httpServletRequest.getSession().getAttribute("email").toString();
            return new ResponseEntity<>(eventService.registerEvent(newEventDto,email), httpHeaders, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(e.toString());
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
