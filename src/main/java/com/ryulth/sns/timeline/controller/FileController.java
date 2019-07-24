package com.ryulth.sns.timeline.controller;


import com.ryulth.sns.timeline.service.FileService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@Slf4j
@RestController
@Api(value = "FileController")
public class FileController {
    private static final HttpHeaders httpHeaders = new HttpHeaders();
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
    }


    @PostMapping("files/image")
    @ResponseBody
    public ResponseEntity imageUpload(
            HttpServletRequest httpServletRequest,
            @RequestParam("data") MultipartFile multipartFile
    ) {
        try {
            String email = httpServletRequest.getSession().getAttribute("email").toString();
            return new ResponseEntity<>(fileService.getImageUploadUrl(multipartFile, email), httpHeaders, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error(e.toString());
            return new ResponseEntity<>(Collections.singletonMap("error", "INTERNAL SERVER ERROR"), httpHeaders, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}