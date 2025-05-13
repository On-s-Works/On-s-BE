package com.ons.back.application.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.ons.back.commons.exception.ApplicationException;
import com.ons.back.commons.exception.payload.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StorageService {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadImage(MultipartFile file) {
        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        try {
            amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
        } catch (IOException e) {
            throw new ApplicationException(
                    ErrorStatus.toErrorStatus("s3 업로드 중 오류 발생", 500, LocalDateTime.now())
            );
        }
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    private String createFileName(String originalFileName) {
        return UUID.randomUUID() + "_" + originalFileName;
    }

    public void deleteImage(String imageUrl) {

        String fileName = extractFileNameFromUrl(imageUrl);

        if (amazonS3Client.doesObjectExist(bucket, fileName)) {
            amazonS3Client.deleteObject(bucket, fileName);
        } else {
            log.error("해당 이미지가 존재하지 않습니다: {} ", fileName);
        }
    }

    // S3 URL에서 파일 이름 추출
    private String extractFileNameFromUrl(String imageUrl) {
        URI uri = URI.create(imageUrl);
        String path = uri.getPath().substring(1);
        return URLDecoder.decode(path, StandardCharsets.UTF_8);
    }
}
