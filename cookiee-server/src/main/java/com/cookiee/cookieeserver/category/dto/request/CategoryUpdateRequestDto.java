package com.cookiee.cookieeserver.category.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryUpdateRequestDto {
    private String categoryName;
    private String categoryColor;

    @Builder
    public CategoryUpdateRequestDto(String categoryName, String categoryColor){
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
    }
}
