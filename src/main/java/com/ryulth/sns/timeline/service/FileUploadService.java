package com.ryulth.sns.timeline.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface FileUploadService {
    String getUploadUrl(MultipartFile multipartFile, String dirName) throws IOException;
}
