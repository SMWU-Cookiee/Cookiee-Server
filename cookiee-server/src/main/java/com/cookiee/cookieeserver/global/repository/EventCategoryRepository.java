package com.cookiee.cookieeserver.global.repository;

import com.cookiee.cookieeserver.category.domain.Category;
import com.cookiee.cookieeserver.global.domain.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventCategoryRepository extends JpaRepository<EventCategory, Integer> {
    @Query("select c from EventCategory c " +
            "join fetch c.event " +
            "where c.category = :category ")
    List<EventCategory> findByCategoryId(@Param("category") Category category);

    List<EventCategory> findEventCategoriesByEventEventId(Long eventId);

    void deleteByCategoryCategoryId(Long categoryId);
}
