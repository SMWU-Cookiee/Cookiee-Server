package com.cookiee.cookieeserver.service;

import com.cookiee.cookieeserver.controller.S3Uploader;
import com.cookiee.cookieeserver.domain.Thumbnail;
import com.cookiee.cookieeserver.domain.User;
import com.cookiee.cookieeserver.dto.request.ThumbnailRegisterRequestDto;
import com.cookiee.cookieeserver.repository.ThumbnailRepository;
import com.cookiee.cookieeserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class ThumbnailService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final ThumbnailRepository thumbnailRepository;
    @Autowired
    private S3Uploader s3Uploader;

    @Transactional
    public Thumbnail createThumbnail(MultipartFile thumbnailUrl, ThumbnailRegisterRequestDto thumbnailRegisterRequestDto, Long userId) throws IOException {
        User user = userRepository.findByUserId(userId);
        Thumbnail savedThumbnail;
        String storedFileName = null;
        if (!thumbnailUrl.isEmpty())
            storedFileName = s3Uploader.saveFile(thumbnailUrl);
        savedThumbnail = thumbnailRepository.save(thumbnailRegisterRequestDto.toEntity(user, storedFileName));

        return savedThumbnail;
    }
}
