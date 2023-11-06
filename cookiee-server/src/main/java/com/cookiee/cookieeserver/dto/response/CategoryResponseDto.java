package com.cookiee.cookieeserver.dto.response;

import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SqlResultSetMapping;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@NamedQuery(
        name = "get_category_dto",
        query = "SELECT category_id as categoryId, category_name as categoryName, category_color as categoryColor) " +
        "FROM category WHERE user_id = :userId"
)
@SqlResultSetMapping(
        name = "category_dto",
        classes = @ConstructorResult(
                targetClass = CategoryResponseDto.class,
                columns = {
                        @ColumnResult(name = "categoryId", type = Integer.class),
                        @ColumnResult(name = "categoryName", type = String.class),
                        @ColumnResult(name = "categoryColor", type = String.class)
                }
        )
)

public class CategoryResponseDto {
    int categoryId;
    String categoryName;
    String categoryColor;

    public CategoryResponseDto(int categoryId, String categoryName, String categoryColor){
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryColor = categoryColor;
    }
}
