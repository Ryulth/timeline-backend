package com.ryulth.sns.timeline.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Service
public class FileService {

    private final FileUploadService fileUploadService;

    public FileService(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    public Map<String, Object> getImageUploadUrl(MultipartFile multipartFile,String email) throws IOException {
        return Collections.singletonMap("src", fileUploadService.getUploadUrl(multipartFile,"images"));
    }
}
