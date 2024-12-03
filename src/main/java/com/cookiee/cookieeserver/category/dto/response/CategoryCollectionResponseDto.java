package com.cookiee.cookieeserver.category.dto.response;

import com.cookiee.cookieeserver.category.domain.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryCollectionResponseDto {
    private Long categoryId;
    private String categoryName;
    private String categoryColor;
    private Boolean collectionExist;

    @Builder
    public CategoryCollectionResponseDto(CategoryResponseDto category, Boolean collectionExist) {
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
        this.categoryColor = category.getCategoryColor();
        this.collectionExist = collectionExist;
    }

}
