package com.cookiee.cookieeserver.category.dto.request;

import com.cookiee.cookieeserver.category.domain.Category;
import com.cookiee.cookieeserver.user.domain.UserV1;
import com.cookiee.cookieeserver.user.domain.UserV2;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryCreateRequestDto {
    private UserV2 userV2;
    private String categoryName;
    private String categoryColor;

    @Builder
    public CategoryCreateRequestDto(String categoryName, String categoryColor){
//        this.user = user;
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
    }

    public Category toEntity(UserV1 userV1){
        return Category.builder()
                .userV1(userV1)
                .categoryName(categoryName)
                .categoryColor(categoryColor)
                .build();
    }
}
