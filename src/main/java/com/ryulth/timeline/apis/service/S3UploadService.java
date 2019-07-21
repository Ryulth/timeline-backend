package com.ryulth.timeline.apis.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@PropertySource("classpath:aws.properties")
public class S3UploadService implements FileUploadService{

    private final AmazonS3 amazonS3Client;
    private @Value("${cloud.aws.s3.bucket}")
    String bucket;

    public S3UploadService(AmazonS3  amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    @Override
    public String getUploadUrl(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException("MultipartFile -> File로 전환이 실패했습니다."));

        return s3Uploader(uploadFile,dirName);
    }

    private String s3Uploader(File uploadFile, String dirName){
        String fileName = dirName + "/" + uploadFile.getName();
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        String uploadUrl = amazonS3Client.getUrl(bucket,fileName).toExternalForm();
        removeNewFile(uploadFile);
        return uploadUrl;
    }

    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("파일이 삭제되었습니다.");
        } else {
            log.info("파일이 삭제되지 못했습니다.");
        }
    }

    private Optional<File> convert(MultipartFile file) throws IOException {
        File convertFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        if(convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }

        return Optional.empty();
    }
}
