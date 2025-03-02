package com.dongsan.file.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3FileService {
    private static final Logger log = LoggerFactory.getLogger(S3FileService.class);
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 amazonS3;

    public S3FileService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    // 파일 Null 확인
    private void checkFileIsNull(MultipartFile file){
        if(file == null){
            throw new RuntimeException("file is Empty");
        }
    }

    // 단일 파일 저장
    public String saveFile(MultipartFile file) {
        checkFileIsNull(file);

        // 파일 확장자를 기반으로 ContentType 설정
        String randomFilename;
        if (file.getOriginalFilename() != null) {
            randomFilename = generateRandomFilename(file.getOriginalFilename());
        } else {
            // 파일 또는 파일 이름이 null인 경우의 처리
            throw new RuntimeException("file is null");
        }

        log.info("File upload started: " + randomFilename);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        String contentType = getContentType(getFileExtension(randomFilename));
        metadata.setContentType(contentType);

        try {
            amazonS3.putObject(bucket, randomFilename, file.getInputStream(), metadata);
        } catch (AmazonS3Exception e) {
            log.error("Amazon S3 error while uploading file: " + e.getMessage());
            throw new RuntimeException("Amazon S3 error while uploading file");
        } catch (SdkClientException e) {
            log.error("AWS SDK client error while uploading file: " + e.getMessage());
            throw new RuntimeException("AWS SDK client error while uploading file");
        } catch (IOException e) {
            log.error("IO error while uploading file: " + e.getMessage());
            throw new RuntimeException("IO error while uploading file");
        }

        log.info("File upload completed: " + randomFilename);

        return amazonS3.getUrl(bucket, randomFilename).toString();
    }

    // 랜덤파일명 생성 (파일명 중복 방지)
    private String generateRandomFilename(String originalFilename) {
        String fileExtension = validateFileExtension(originalFilename);
        return UUID.randomUUID() + "." + fileExtension;
    }

    // 파일 확장자 체크
    private String validateFileExtension(String originalFilename) {
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        List<String> allowedExtensions = Arrays.asList("jpg", "png", "gif", "jpeg");

        if (!allowedExtensions.contains(fileExtension)) {
            throw new RuntimeException("fail upload 4");
        }
        return fileExtension;
    }

    // 파일 확장자를 추출하는 메소드
    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }

    // 파일 확장자에 따른 ContentType을 반환하는 메소드
    private String getContentType(String fileExtension) {
        Map<String, String> contentTypeMap = new HashMap<>();
        contentTypeMap.put("jpg", "image/jpeg");
        contentTypeMap.put("jpeg", "image/jpeg");
        contentTypeMap.put("png", "image/png");
        contentTypeMap.put("gif", "image/gif");

        return contentTypeMap.getOrDefault(fileExtension, "application/octet-stream");
    }

}
