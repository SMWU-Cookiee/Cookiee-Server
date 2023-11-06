package com.cookiee.cookieeserver.repository;

import com.cookiee.cookieeserver.domain.Category;
import com.cookiee.cookieeserver.dto.response.CategoryGetResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
    @Query(name = "get_category_dto", nativeQuery = true)
    List<CategoryGetResponseDto> findCategoriesByUserUserId(@Param("userId") int userId);

    boolean existsByCategoryName(String categoryName);

    boolean existsByCategoryColor(String categoryColor);
}
