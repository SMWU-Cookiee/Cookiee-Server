package com.cookiee.cookieeserver.repository;

import com.cookiee.cookieeserver.domain.Event;
import com.cookiee.cookieeserver.domain.Thumbnail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThumbnailRepository extends JpaRepository<Thumbnail, Long> {
}
