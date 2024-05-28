package com.cookiee.cookieeserver.thumbnail.service;

import com.amazonaws.services.s3.AmazonS3;
import com.cookiee.cookieeserver.event.service.S3Uploader;
import com.cookiee.cookieeserver.global.exception.GeneralException;
import com.cookiee.cookieeserver.thumbnail.domain.Thumbnail;
import com.cookiee.cookieeserver.thumbnail.dto.request.ThumbnailRegisterRequestDto;
import com.cookiee.cookieeserver.thumbnail.dto.response.ThumbnailResponseDto;
import com.cookiee.cookieeserver.thumbnail.repository.ThumbnailRepository;
import com.cookiee.cookieeserver.user.domain.User;
import com.cookiee.cookieeserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
import java.net.URI;

import static com.cookiee.cookieeserver.global.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ThumbnailUserByDeviceService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ThumbnailRepository thumbnailRepository;
    @Autowired
    private S3Uploader s3Uploader;
    private final AmazonS3 amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;


    @Transactional
    public ThumbnailResponseDto createThumbnail(MultipartFile thumbnailImage, ThumbnailRegisterRequestDto thumbnailRegisterRequestDto, String deviceId) {
        User user = getUserByDeviceId(deviceId);
        Long userId = user.getUserId();
        Thumbnail savedThumbnail;
        String storedFileName = null;
        if (!thumbnailImage.isEmpty()) {
            storedFileName = s3Uploader.saveFile(thumbnailImage, String.valueOf(userId), "thumbnail");
            savedThumbnail = thumbnailRepository.save(thumbnailRegisterRequestDto.toEntity(user, storedFileName));
            return ThumbnailResponseDto.from(savedThumbnail);
        } else {
            throw new GeneralException(IMAGE_IS_NULL);
        }
        //return new ThumbnailResponseDto(savedThumbnail.getThumbnailId(), savedThumbnail.getEventYear(), savedThumbnail.getEventMonth(), savedThumbnail.getEventDate(), savedThumbnail.getThumbnailUrl());
    }

    @Transactional
    public List<ThumbnailResponseDto> getThumbnail(String deviceId) {
        Long userId = getUserByDeviceId(deviceId).getUserId();
        List<Thumbnail> thumbnails = thumbnailRepository.findThumbnailsByUserUserId(userId);
        return thumbnails.stream()
                .map(ThumbnailResponseDto::from)
                .collect(Collectors.toList());

    }

    @Transactional
    public void deleteThumbnail(String deviceId, long thumbnailId) {
        Long userId = getUserByDeviceId(deviceId).getUserId();
        Thumbnail deletedthumbnail;
        deletedthumbnail = thumbnailRepository.findByUserUserIdAndThumbnailId(userId, thumbnailId);
        String fileName = extractFileNameFromUrl(deletedthumbnail.getThumbnailUrl());
        amazonS3Client.deleteObject(bucketName, fileName);
        thumbnailRepository.delete(deletedthumbnail);
    }

    // 이미지 URL 파일명 추출
    private static String extractFileNameFromUrl(String imageUrl) {
        try {
            URI uri = new URI(imageUrl);
            String path = uri.getPath();
            return path.substring(path.lastIndexOf('/') + 1);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Transactional
    public ThumbnailResponseDto updateThumbnail(MultipartFile thumbnailUrl, String deviceId, long thumbnailId) {
        Long userId = getUserByDeviceId(deviceId).getUserId();
        Thumbnail updatedthumbnail = thumbnailRepository.findByUserUserIdAndThumbnailId(userId, thumbnailId);
        String fileName = extractFileNameFromUrl(updatedthumbnail.getThumbnailUrl());
        amazonS3Client.deleteObject(bucketName, fileName); //버킷에서 사진 삭제
        String updatedFileName = s3Uploader.saveFile(thumbnailUrl, String.valueOf(userId), "thumbnail");
        updatedthumbnail.update(updatedFileName);
        return new ThumbnailResponseDto(updatedthumbnail.getThumbnailId(), updatedthumbnail.getEventYear(), updatedthumbnail.getEventMonth(), updatedthumbnail.getEventDate(), updatedthumbnail.getThumbnailUrl());
    }

    public User getUserByDeviceId(String deviceId) {
        return userRepository.findByDeviceId(deviceId)
                .orElseThrow(() -> new GeneralException(USER_NOT_FOUND));
    }
}