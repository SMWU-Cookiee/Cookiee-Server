package com.cookiee.cookieeserver.thumbnail.repository;

import com.cookiee.cookieeserver.thumbnail.domain.Thumbnail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ThumbnailRepository extends JpaRepository<Thumbnail, Long> {
    List<Thumbnail> findThumbnailsByUserUserId(@Param("userId") long userId);

    Thumbnail findByUserUserIdAndThumbnailId(long userId, long thumbnailId);

    Thumbnail findByUserUserIdAndEventYearAndEventMonthAndEventDate(long uerId, int year, int month, int day);
}
