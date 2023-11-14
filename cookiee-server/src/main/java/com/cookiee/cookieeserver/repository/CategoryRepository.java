package com.cookiee.cookieeserver.repository;

import com.cookiee.cookieeserver.domain.Category;
import com.cookiee.cookieeserver.dto.response.CategoryResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query(name = "get_category_dto", nativeQuery = true)
    List<CategoryResponseDto> findCategoriesByUserUserId(@Param("userId") int userId);
    //SELECT CASE  WHEN count(pl)> 0 THEN true ELSE false END FROM PostboxLabel pl ...
    @Query(value = "SELECT COUNT(category_id) > 0 " +
            "FROM category " +
            "WHERE category.category_id = :categoryId AND category.user_id = :userId",
    nativeQuery = true)
    int existsByCategoryIdInUser(@Param("userId") int userId, @Param("categoryId") int categoryId);

    boolean existsByCategoryName(String categoryName);

    boolean existsByCategoryColor(String categoryColor);

}
