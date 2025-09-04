package com.dongsan.file.service;

import static org.assertj.core.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import com.amazonaws.services.s3.AmazonS3;
import com.dongsan.file.config.S3Config;

@SpringBootTest(classes = { S3FileService.class, S3Config.class })
@ActiveProfiles({"dev", "s3"})
class S3FileServiceIntegrationTest {

    @Autowired
    private S3FileService s3FileService;

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private String uploadedKey;

    @AfterEach
    void tearDown() {
        if (uploadedKey != null) {
            amazonS3.deleteObject(bucket, uploadedKey);
        }
    }

    @Test
    void saveFile_실제S3_업로드_URL반환_및_객체존재확인() throws Exception {
        byte[] bytes = ("hello s3 " + UUID.randomUUID()).getBytes(StandardCharsets.UTF_8);
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "sample.png",
                "image/png",
                bytes
        );

        String url = s3FileService.saveFile(file);

        assertThat(url).isNotBlank();

        // 업로드된 key 추출 (마지막 / 뒤)
        String key = url.substring(url.lastIndexOf('/') + 1);
        uploadedKey = key;

        // 실제 객체 존재 여부
        assertThat(amazonS3.doesObjectExist(bucket, key)).isTrue();
    }
}
