package com.cookiee.cookieeserver.event.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.cookiee.cookieeserver.global.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static com.cookiee.cookieeserver.global.ErrorCode.*;


@Slf4j
@RequiredArgsConstructor    // final 멤버변수가 있으면 생성자 항목에 포함시킴
@Component
@Service
public class S3Uploader {

    private final AmazonS3 amazonS3Client;

    @Value("cookiee-s3")
    private String bucket;

    public String saveFile(MultipartFile multipartFile, String userId, String dirName) {
        String originalFilename = multipartFile.getOriginalFilename();
        String newFilename = userId + "/" + dirName + "/" + UUID.randomUUID() + originalFilename;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        try {
            amazonS3Client.putObject(bucket, newFilename, multipartFile.getInputStream(), metadata);
        } catch (IOException e) {
            throw new GeneralException(S3_UPLOAD_ERROR);
        }

        return amazonS3Client.getUrl(bucket, newFilename).toString();
    }
}
