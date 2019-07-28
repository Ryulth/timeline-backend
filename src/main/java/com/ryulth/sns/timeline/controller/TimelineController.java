package com.ryulth.sns.timeline.controller;

import com.ryulth.sns.config.UnauthorizedException;
import com.ryulth.sns.timeline.service.TimelineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Slf4j
@RestController
@Api(value = "TimelineController")
public class TimelineController {
    private static final HttpHeaders httpHeaders = new HttpHeaders();
    private final TimelineService timelineService;
    public TimelineController(TimelineService timelineService) {
        this.timelineService = timelineService;
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    }

    @GetMapping("timeline/{authorEmail}")
    @ApiOperation(value = "Get Timeline API", notes = "각 유저의 타임라인을 가져온다.")
    public ResponseEntity getUserTimeline(
            HttpServletRequest httpServletRequest,
            @PathVariable("authorEmail") String authorEmail,
            @RequestParam("page") int page
    ) {
        try {
            String accessEmail = httpServletRequest.getSession().getAttribute("email").toString();
            return new ResponseEntity<>(timelineService.getUserTimeline(accessEmail,authorEmail,page), httpHeaders, HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), httpHeaders, HttpStatus.UNAUTHORIZED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(Collections.singletonMap("error", e.getMessage()), httpHeaders, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("timeline")
    @ApiOperation(value = "Get Timeline API", notes = "각 유저의 타임라인을 가져온다.")
    public ResponseEntity getUserTimeline(
            HttpServletRequest httpServletRequest,
            @RequestParam("page") int page
    ) {
        try {
            String accessEmail = httpServletRequest.getSession().getAttribute("email").toString();
            return new ResponseEntity<>(timelineService.getTimeline(accessEmail,page), httpHeaders, HttpStatus.OK);
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
