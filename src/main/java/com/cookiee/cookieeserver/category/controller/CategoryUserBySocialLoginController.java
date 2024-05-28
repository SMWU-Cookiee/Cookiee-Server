
package com.cookiee.cookieeserver.category.controller;

import com.cookiee.cookieeserver.category.domain.Category;
import com.cookiee.cookieeserver.login.jwt.JwtService;
import com.cookiee.cookieeserver.global.dto.BaseResponseDto;
import com.cookiee.cookieeserver.category.dto.request.CategoryCreateRequestDto;
import com.cookiee.cookieeserver.category.dto.request.CategoryUpdateRequestDto;
import com.cookiee.cookieeserver.category.dto.response.CategoryResponseDto;
import com.cookiee.cookieeserver.event.dto.response.EventCategoryGetResponseDto;
import com.cookiee.cookieeserver.category.service.CategoryUserBySocialLoginService;
import com.cookiee.cookieeserver.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cookiee.cookieeserver.global.SuccessCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="api/v2/categories/")
public class CategoryUserBySocialLoginController {
    private final CategoryUserBySocialLoginService categoryUserBySocialLoginService;
    private final JwtService jwtService;

/***
     * 카테고리 등록
     * @param userId        유저 아이디
     * @param requestDto    등록하려는 카테고리 내용
     * @return              CategoryResponseDto

     */

    @PostMapping("{userId}")

    public BaseResponseDto<CategoryResponseDto> postCategoryUserBySocialLogin(@PathVariable Long userId,
                                                             @RequestBody CategoryCreateRequestDto requestDto){
        final User currentUser = jwtService.getAndValidateCurrentUser(userId);

        Category newCategory = categoryUserBySocialLoginService.create(currentUser, requestDto);
        CategoryResponseDto categoryResponseDto = CategoryResponseDto.builder()
                .categoryId(newCategory.getCategoryId())
                .categoryName(newCategory.getCategoryName())
                .categoryColor(newCategory.getCategoryColor())
                .build();

        return BaseResponseDto.ofSuccess(CREATE_CATEGORY_SUCCESS, categoryResponseDto);
    }

    // 특정 유저의 카테고리 전체 조회
    @GetMapping("{userId}")
    public BaseResponseDto<List<CategoryResponseDto>> getCategoryUserBySocialLogin(@PathVariable Long userId){
        final User currentUser = jwtService.getAndValidateCurrentUser(userId);

        List<CategoryResponseDto> result;
        result = categoryUserBySocialLoginService.getAllCategories(currentUser);

        return BaseResponseDto.ofSuccess(GET_CATEGORY_SUCCESS, result);
    }

    // 카테고리 수정
    @PutMapping("{userId}/{categoryId}")
    public BaseResponseDto<CategoryResponseDto> updateCategoryUserBySocialLogin(@PathVariable Long userId,
                                                               @PathVariable Long categoryId,
                                                               @RequestBody CategoryUpdateRequestDto requestDto){
        final User currentUser = jwtService.getAndValidateCurrentUser(userId);

        CategoryResponseDto result = categoryUserBySocialLoginService.update(currentUser, categoryId, requestDto);

        return BaseResponseDto.ofSuccess(MODIFY_CATEGORY_SUCCESS, result);
    }

    // 카테고리 삭제
    @DeleteMapping("{userId}/{categoryId}")
    public BaseResponseDto<?> deleteCategoryUserBySocialLogin(@PathVariable Long userId,
                                             @PathVariable Long categoryId){
        final User currentUser = jwtService.getAndValidateCurrentUser(userId);

        categoryUserBySocialLoginService.delete(currentUser, categoryId);

        return BaseResponseDto.ofSuccess(DELETE_CATEGORY_SUCCESS);
    }

    // 카테고리 모아보기....
    @GetMapping("/collection/{userId}/{categoryId}")
    public BaseResponseDto<EventCategoryGetResponseDto> getCollectionUserBySocialLogin(@PathVariable Long userId,
                                                                      @PathVariable Long categoryId){
        final User currentUser = jwtService.getAndValidateCurrentUser(userId);

        // 모아보기 데이터 리턴
        EventCategoryGetResponseDto result = categoryUserBySocialLoginService.findByIdForCollection(currentUser, categoryId);

        return BaseResponseDto.ofSuccess(GET_COLLECTION_SUCCESS, result);
    }
}
