package com.cookiee.cookieeserver.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.cookiee.cookieeserver.controller.S3Uploader;
import com.cookiee.cookieeserver.domain.Thumbnail;
import com.cookiee.cookieeserver.domain.User;
import com.cookiee.cookieeserver.dto.request.ThumbnailRegisterRequestDto;
import com.cookiee.cookieeserver.dto.request.ThumbnailUpdateRequestDto;
import com.cookiee.cookieeserver.dto.response.ThumbnailGetResponseDto;
import com.cookiee.cookieeserver.dto.response.ThumbnailUpdateResponseDto;
import com.cookiee.cookieeserver.repository.ThumbnailRepository;
import com.cookiee.cookieeserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        User user = userRepository.findByUserId(userId).orElseThrow(
                () -> new IllegalArgumentException("해당 id의 사용자가 없습니다.")
        );
        Thumbnail savedThumbnail;
        String storedFileName = null;
        if (!thumbnailUrl.isEmpty())
            storedFileName = s3Uploader.saveFile(thumbnailUrl);
        savedThumbnail = thumbnailRepository.save(thumbnailRegisterRequestDto.toEntity(user, storedFileName));

        return savedThumbnail;
    }

    @Transactional
    public List<ThumbnailGetResponseDto> getThumbnail(long userId){
        List<Thumbnail> thumbnails = thumbnailRepository.findThumbnailsByUserUserId((int) userId);
        return thumbnails.stream()
                .map(ThumbnailGetResponseDto::from)
                .collect(Collectors.toList());

    }

    @Transactional
    public void deleteThumbnail(long userId, long thumbnailId){
        Thumbnail thumbnail;
        thumbnail = thumbnailRepository.findByUserUserIdAndThumbnailId((int)userId, thumbnailId);
        thumbnailRepository.delete(thumbnail);

    }

    @Transactional
    public ThumbnailUpdateResponseDto updateThumbnail(MultipartFile thumbnailUrl, ThumbnailUpdateRequestDto thumbnailUpdateRequestDto, int userId, long thumbnaild) {
        Thumbnail updatedThumbnail = thumbnailRepository.findByUserUserIdAndThumbnailId(userId, thumbnaild);
        String updatedFileName = null;
        if (thumbnailUrl != null){
            updatedFileName = s3Uploader.saveFile(thumbnailUrl);
            updatedThumbnail = thumbnailRepository.save(thumbnailUpdateRequestDto.toEntity(updatedThumbnail, updatedFileName));
        }
        else{
            throw new NotFoundException("이미지가 없습니다.");
        }
        return new ThumbnailUpdateResponseDto(thumbnail.getThumbnailId(), thumbnail.getThumbnailUrl());
    }
}
