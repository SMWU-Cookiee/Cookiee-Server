package com.cookiee.cookieeserver.controller;

import com.amazonaws.services.kms.model.NotFoundException;
import com.cookiee.cookieeserver.constant.StatusCode;
import com.cookiee.cookieeserver.domain.Category;
import com.cookiee.cookieeserver.domain.User;
import com.cookiee.cookieeserver.dto.BaseResponseDto;
import com.cookiee.cookieeserver.dto.DataResponseDto;
import com.cookiee.cookieeserver.dto.ErrorResponseDto;
import com.cookiee.cookieeserver.dto.request.CategoryCreateRequestDto;
import com.cookiee.cookieeserver.dto.request.CategoryUpdateRequestDto;
import com.cookiee.cookieeserver.dto.response.CategoryResponseDto;
import com.cookiee.cookieeserver.dto.response.EventCategoryGetResponseDto;
import com.cookiee.cookieeserver.repository.CategoryRepository;
import com.cookiee.cookieeserver.service.CategoryService;
import com.cookiee.cookieeserver.service.UserService;
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
        try {
            Optional<User> user = userService.findOneById(userId);
            if(user.isEmpty()){
                return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "해당 id의 사용자가 존재하지 않습니다.");
            }
            else{
                Category newCategory = categoryService.create(user.get(), requestDto);
                categoryResponseDto = CategoryResponseDto.builder()
                        .categoryId(newCategory.getCategoryId())
                        .categoryName(newCategory.getCategoryName())
                        .categoryColor(newCategory.getCategoryColor())
                        .build();
            }
        }
        catch (Exception e){
            return ErrorResponseDto.of(StatusCode.BAD_REQUEST, e.getMessage());
        }
        return DataResponseDto.of(categoryResponseDto, "카테고리 등록에 성공하였습니다.");
    }

    // 특정 유저의 카테고리 전체 조회
    @GetMapping("/category/{userId}")
    public BaseResponseDto<List<CategoryResponseDto>> getCategory(@PathVariable Long userId){
        List<CategoryResponseDto> result;
        try {
            Optional<User> user = userService.findOneById(userId);
            if(user.isEmpty()){
                return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "해당 id의 사용자가 존재하지 않습니다.");
            }
            else{
                try{
                    result = categoryService.getAllCategories(userId);
                }
                catch (Exception e) {
                    return ErrorResponseDto.of(StatusCode.INTERNAL_ERROR, "해당 사용자의 카테고리 조회 요청에 실패하였습니다.");
                }
            }
        }
        catch(Exception e){
            return ErrorResponseDto.of(StatusCode.INTERNAL_ERROR, e.getMessage());
        }

        return DataResponseDto.of(result, "카테고리 조회 요청에 성공하였습니다.");
    }

    // 카테고리 수정
    @PutMapping("/category/{userId}/{categoryId}")
    public BaseResponseDto<CategoryResponseDto> updateCategory(@PathVariable Long userId,
                                                    @PathVariable Long categoryId,
                                                    @RequestBody CategoryUpdateRequestDto requestDto){
        CategoryResponseDto result;

        try{
            Optional<User> user = userService.findOneById(userId);
            if(user.isEmpty()){
                return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "해당 id의 사용자가 존재하지 않습니다.");
            }
            else {
                try {
                    result = categoryService.update(userId, categoryId, requestDto);
                } catch (NotFoundException e) {
                    return ErrorResponseDto.of(StatusCode.NOT_FOUND, e.getMessage());
                }
                catch (Exception e){
                    return ErrorResponseDto.of(StatusCode.INTERNAL_ERROR, e.getMessage());
                }
            }
        }
        catch(Exception e){
            return ErrorResponseDto.of(StatusCode.INTERNAL_ERROR, e.getMessage());
        }

        return DataResponseDto.of(result, "카테고리 수정에 성공하였습니다.");
    }

    // 카테고리 삭제
    @DeleteMapping("/category/{userId}/{categoryId}")
    public BaseResponseDto deleteCategory(@PathVariable Long userId,
                                          @PathVariable Long categoryId){
        try {
            User user = userService.findOneById(userId)
                    .orElseThrow(()-> new IllegalArgumentException("해당 id의 사용자가 존재하지 않습니다."));
            categoryService.delete(userId, categoryId);
        }
        catch(NotFoundException e){
            return ErrorResponseDto.of(StatusCode.NOT_FOUND, e.getMessage());
        }
        catch(Exception e){
            return ErrorResponseDto.of(StatusCode.INTERNAL_ERROR, e.getMessage());
        }

        return DataResponseDto.of(null, "카테고리 삭제에 성공하였습니다.");
    }

    // 카테고리 모아보기....
    @GetMapping("/collection/{userId}/{categoryId}")
    public BaseResponseDto<EventCategoryGetResponseDto> getCollection(@PathVariable Long userId,
                                                                      @PathVariable Long categoryId){
        EventCategoryGetResponseDto result;

        try {
            // 유저 유효성 먼저 확인
//            User user = userService.findOneById(userId)
//                    .orElseThrow(()-> new IllegalArgumentException("해당 id의 사용자가 존재하지 않습니다."));
//
//            final AtomicBoolean[] isValidCategoryId = {new AtomicBoolean(false)};
//
//            // 카테고리 유효성 먼저 확인
//            Category category = categoryService.findOneById(categoryId).orElseThrow(
//                    () -> new IllegalArgumentException("해당 id의 카테고리가 존재하지 않습니다.")
//            );

            // 모아보기 데이터 리턴
            result = categoryService.findByIdForCollection(categoryId);
        }
        catch(NotFoundException e){
            return ErrorResponseDto.of(StatusCode.NOT_FOUND, e.getMessage());
        }
        catch(Exception e){
            return ErrorResponseDto.of(StatusCode.INTERNAL_ERROR, e.getMessage());
        }

        return DataResponseDto.of(result, "카테고리 모아보기 조회 요청에 성공하였습니다.");
    }
}
