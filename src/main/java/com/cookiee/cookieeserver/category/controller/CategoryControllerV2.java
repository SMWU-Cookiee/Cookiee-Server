
package com.cookiee.cookieeserver.category.controller;

import com.cookiee.cookieeserver.category.domain.Category;
import com.cookiee.cookieeserver.login.jwt.JwtService;
import com.cookiee.cookieeserver.user.domain.UserV2;
import com.cookiee.cookieeserver.global.dto.BaseResponseDto;
import com.cookiee.cookieeserver.category.dto.request.CategoryCreateRequestDto;
import com.cookiee.cookieeserver.category.dto.request.CategoryUpdateRequestDto;
import com.cookiee.cookieeserver.category.dto.response.CategoryResponseDto;
import com.cookiee.cookieeserver.event.dto.response.EventCategoryGetResponseDto;
import com.cookiee.cookieeserver.category.service.CategoryServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cookiee.cookieeserver.global.SuccessCode.*;

@RestController
@RequiredArgsConstructor
public class CategoryControllerV2 {
    private final CategoryServiceV2 categoryServiceV2;
    private final JwtService jwtService;

    */
/***
     * 카테고리 등록
     * @param userId        유저 아이디
     * @param requestDto    등록하려는 카테고리 내용
     * @return              CategoryResponseDto
     *//*

    @PostMapping("/category/{userId}")
    public BaseResponseDto<CategoryResponseDto> postCategory(@PathVariable Long userId,
                                                             @RequestBody CategoryCreateRequestDto requestDto){
        final UserV2 currentUserV2 = jwtService.getAndValidateCurrentUser(userId);

        Category newCategory = categoryServiceV2.create(currentUserV2, requestDto);
        CategoryResponseDto categoryResponseDto = CategoryResponseDto.builder()
                .categoryId(newCategory.getCategoryId())
                .categoryName(newCategory.getCategoryName())
                .categoryColor(newCategory.getCategoryColor())
                .build();

        return BaseResponseDto.ofSuccess(CREATE_CATEGORY_SUCCESS, categoryResponseDto);
    }

    // 특정 유저의 카테고리 전체 조회
    @GetMapping("/category/{userId}")
    public BaseResponseDto<List<CategoryResponseDto>> getCategory(@PathVariable Long userId){
        final UserV2 currentUserV2 = jwtService.getAndValidateCurrentUser(userId);

        List<CategoryResponseDto> result;
        result = categoryServiceV2.getAllCategories(currentUserV2);

        return BaseResponseDto.ofSuccess(GET_CATEGORY_SUCCESS, result);
    }

    // 카테고리 수정
    @PutMapping("/category/{userId}/{categoryId}")
    public BaseResponseDto<CategoryResponseDto> updateCategory(@PathVariable Long userId,
                                                               @PathVariable Long categoryId,
                                                               @RequestBody CategoryUpdateRequestDto requestDto){
        final UserV2 currentUserV2 = jwtService.getAndValidateCurrentUser(userId);

        CategoryResponseDto result = categoryServiceV2.update(currentUserV2, categoryId, requestDto);

        return BaseResponseDto.ofSuccess(MODIFY_CATEGORY_SUCCESS, result);
    }

    // 카테고리 삭제
    @DeleteMapping("/category/{userId}/{categoryId}")
    public BaseResponseDto<?> deleteCategory(@PathVariable Long userId,
                                             @PathVariable Long categoryId){
        final UserV2 currentUserV2 = jwtService.getAndValidateCurrentUser(userId);

        categoryServiceV2.delete(currentUserV2, categoryId);

        return BaseResponseDto.ofSuccess(DELETE_CATEGORY_SUCCESS);
    }

    // 카테고리 모아보기....
    @GetMapping("/collection/{userId}/{categoryId}")
    public BaseResponseDto<EventCategoryGetResponseDto> getCollection(@PathVariable Long userId,
                                                                      @PathVariable Long categoryId){
        final UserV2 currentUserV2 = jwtService.getAndValidateCurrentUser(userId);

        // 모아보기 데이터 리턴
        EventCategoryGetResponseDto result = categoryServiceV2.findByIdForCollection(currentUserV2, categoryId);

        return BaseResponseDto.ofSuccess(GET_COLLECTION_SUCCESS, result);
    }
}
