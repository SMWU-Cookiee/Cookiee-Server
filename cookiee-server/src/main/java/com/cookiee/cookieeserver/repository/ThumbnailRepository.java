package com.cookiee.cookieeserver.repository;

import com.cookiee.cookieeserver.domain.Event;
import com.cookiee.cookieeserver.domain.Thumbnail;
import com.cookiee.cookieeserver.dto.response.ThumbnailGetResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ThumbnailRepository extends JpaRepository<Thumbnail, Long> {
    List<Thumbnail> findThumbnailsByUserUserId(@Param("userId") int userId);

    Optional<Thumbnail> findByUserUserIdAndThumbnailId(int userId, long thumbnailId);
}
