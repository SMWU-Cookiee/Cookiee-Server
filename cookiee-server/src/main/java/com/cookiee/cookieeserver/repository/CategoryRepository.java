package com.cookiee.cookieeserver.repository;

import com.cookiee.cookieeserver.domain.Category;
import com.cookiee.cookieeserver.domain.EventCategory;
import com.cookiee.cookieeserver.domain.User;
import com.cookiee.cookieeserver.dto.response.CategoryResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query(name = "get_category_dto", nativeQuery = true)
    List<CategoryResponseDto> findCategoriesByUserUserId(@Param("userId") Long userId);
    //SELECT CASE  WHEN count(pl)> 0 THEN true ELSE false END FROM PostboxLabel pl ...
    @Query(value = "SELECT COUNT(category_id) > 0 " +
            "FROM category " +
            "WHERE category.category_id = :categoryId AND category.user_id = :userId",
    nativeQuery = true)
    int existsByCategoryIdInUser(@Param("userId") Long userId, @Param("categoryId") Long categoryId);

    boolean existsByCategoryName(String categoryName);

    boolean existsByCategoryColor(String categoryColor);

    Optional<Category> findByCategoryId(Long categoryId);

    @Query(value = "SELECT category_id as categoryId, category_name as categoryName, category_color as categoryColor) " +
            "FROM category WHERE category_id IN (:categoryIDs)",
            nativeQuery = true)
    List<Category> findAllByCategoryIdList(List<Long> categoryIDs);
}
