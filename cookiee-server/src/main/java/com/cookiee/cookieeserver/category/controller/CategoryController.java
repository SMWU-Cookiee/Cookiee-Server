package com.cookiee.cookieeserver.category.controller;

import com.cookiee.cookieeserver.category.domain.Category;
import com.cookiee.cookieeserver.global.StatusCode;
import com.cookiee.cookieeserver.global.exception.GeneralException;
import com.cookiee.cookieeserver.user.domain.User;
import com.cookiee.cookieeserver.global.dto.BaseResponseDto;
import com.cookiee.cookieeserver.global.dto.DataResponseDto;
import com.cookiee.cookieeserver.global.dto.ErrorResponseDto;
import com.cookiee.cookieeserver.category.dto.request.CategoryCreateRequestDto;
import com.cookiee.cookieeserver.category.dto.request.CategoryUpdateRequestDto;
import com.cookiee.cookieeserver.category.dto.response.CategoryResponseDto;
import com.cookiee.cookieeserver.event.dto.response.EventCategoryGetResponseDto;
import com.cookiee.cookieeserver.category.repository.CategoryRepository;
import com.cookiee.cookieeserver.category.service.CategoryService;
import com.cookiee.cookieeserver.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final UserService userService;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    /***
     * 카테고리 등록
     * @param userId        유저 아이디
     * @param requestDto    등록하려는 카테고리 내용
     * @return              CategoryResponseDto
     */
    @PostMapping("/category/{userId}")
    public BaseResponseDto<CategoryResponseDto> postCategory(@PathVariable Long userId,
                                                  @RequestBody CategoryCreateRequestDto requestDto){
        CategoryResponseDto categoryResponseDto;
        User user = userService.findOneById(userId);
//        if (user.isEmpty()) {
//            return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "해당 id의 사용자가 존재하지 않습니다.");
//        } else {
            Category newCategory = categoryService.create(user, requestDto);
            categoryResponseDto = CategoryResponseDto.builder()
                    .categoryId(newCategory.getCategoryId())
                    .categoryName(newCategory.getCategoryName())
                    .categoryColor(newCategory.getCategoryColor())
                    .build();
//        }
        return DataResponseDto.of(categoryResponseDto, "카테고리 등록에 성공하였습니다.");
    }

    // 특정 유저의 카테고리 전체 조회
    @GetMapping("/category/{userId}")
    public BaseResponseDto<List<CategoryResponseDto>> getCategory(@PathVariable Long userId){
        List<CategoryResponseDto> result;
//        User user = userService.findOneById(userId);
//        if(user.isEmpty()){
//            return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "해당 id의 사용자가 존재하지 않습니다.");
//        }
//        else{
                result = categoryService.getAllCategories(userId);
//        }

        return DataResponseDto.of(result, "카테고리 조회 요청에 성공하였습니다.");
    }

    // 카테고리 수정
    @PutMapping("/category/{userId}/{categoryId}")
    public BaseResponseDto<CategoryResponseDto> updateCategory(@PathVariable Long userId,
                                                    @PathVariable Long categoryId,
                                                    @RequestBody CategoryUpdateRequestDto requestDto){
        CategoryResponseDto result;

//        User user = userService.findOneById(userId);
//        if(user.isEmpty()){
//            return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "해당 id의 사용자가 존재하지 않습니다.");
//        }
//        else {
            result = categoryService.update(userId, categoryId, requestDto);
//        }

        return DataResponseDto.of(result, "카테고리 수정에 성공하였습니다.");
    }

    // 카테고리 삭제
    @DeleteMapping("/category/{userId}/{categoryId}")
    public BaseResponseDto deleteCategory(@PathVariable Long userId,
                                          @PathVariable Long categoryId){

//        User user = userService.findOneById(userId)
//                .orElseThrow(()-> new GeneralException(USER_NOT_FOUND);
        categoryService.delete(userId, categoryId);

        return DataResponseDto.of(null, "카테고리 삭제에 성공하였습니다.");
    }

    // 카테고리 모아보기....
    @GetMapping("/collection/{userId}/{categoryId}")
    public BaseResponseDto<EventCategoryGetResponseDto> getCollection(@PathVariable Long userId,
                                                                      @PathVariable Long categoryId){
        EventCategoryGetResponseDto result;

        // 모아보기 데이터 리턴
        result = categoryService.findByIdForCollection(categoryId);

        return DataResponseDto.of(result, "카테고리 모아보기 조회 요청에 성공하였습니다.");
    }
}
