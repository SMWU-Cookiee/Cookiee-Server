package com.cookiee.cookieeserver.repository;

import com.cookiee.cookieeserver.domain.Event;
import com.cookiee.cookieeserver.domain.Thumbnail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ThumbnailRepository extends JpaRepository<Thumbnail, Long> {
    List<Thumbnail> findByUserID(Long userId);

    Optional<Thumbnail> findByUserIDAndThumbnailId(long userId, long thumbnailId);
}
