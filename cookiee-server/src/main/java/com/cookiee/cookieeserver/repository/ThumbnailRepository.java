package com.cookiee.cookieeserver.repository;

import com.cookiee.cookieeserver.domain.Thumbnail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ThumbnailRepository extends JpaRepository<Thumbnail, Long> {
    List<Thumbnail> findThumbnailsByUserUserId(@Param("userId") long userId);

    Thumbnail findByUserUserIdAndThumbnailId(long userId, long thumbnailId);
}
