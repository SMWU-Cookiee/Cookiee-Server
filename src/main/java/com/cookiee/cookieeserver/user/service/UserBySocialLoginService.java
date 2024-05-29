package com.cookiee.cookieeserver.user.service;

import com.amazonaws.services.s3.AmazonS3;
import com.cookiee.cookieeserver.event.service.S3Uploader;
import com.cookiee.cookieeserver.global.exception.GeneralException;
import com.cookiee.cookieeserver.user.domain.User;
import com.cookiee.cookieeserver.user.dto.request.UpdateUserRequestDto;
import com.cookiee.cookieeserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.cookiee.cookieeserver.event.service.EventUserBySocialLoginService.extractFileNameFromUrl;
import static com.cookiee.cookieeserver.global.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class UserBySocialLoginService {
    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;
    private final AmazonS3 amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    public User findOneById(Long userId) {
        Optional<User> user = userRepository.findByUserId(userId);

        if (user.isEmpty())
            throw new GeneralException(USER_NOT_FOUND);

        return user.get();
    }

    public User updateUser(User user, UpdateUserRequestDto requestUser){
        // 프로필 이미지는 null이 아닐 때, 즉 변경 사항이 존재할 때만 수정한다.
        if (requestUser.getProfileImage() != null) {
            // 저장된 이미지부터 삭제
            String fileName = extractFileNameFromUrl(user.getProfileImage());
            amazonS3Client.deleteObject(bucketName, fileName);

            // 프로필 이미지 s3에 생성 후 저장된 파일명 가져오기
            String storedFileName = s3Uploader.saveFile(requestUser.getProfileImage(),
                    String.valueOf(user.getUserId()),
                    "profile");

            user.setProfileImage(storedFileName);
        }
        user.setNickname(requestUser.getNickname());
        user.setSelfDescription(requestUser.getSelfDescription());

        return user;
    }
}