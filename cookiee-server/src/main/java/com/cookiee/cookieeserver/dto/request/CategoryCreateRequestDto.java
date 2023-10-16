package com.cookiee.cookieeserver.dto.request;

import com.cookiee.cookieeserver.domain.Category;
import com.cookiee.cookieeserver.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CategoryCreateRequestDto {
    private User user;
    private String categoryName;
    private String categoryColor;

    @Builder
    public CategoryCreateRequestDto(String categoryName, String categoryColor){
//        this.user = user;
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
    }

    public Category toEntity(User user){
        return Category.builder()
                .user(user)
                .categoryName(categoryName)
                .categoryColor(categoryColor)
                .build();
    }
}
