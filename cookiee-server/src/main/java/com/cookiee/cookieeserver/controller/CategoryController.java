package com.cookiee.cookieeserver.controller;

import com.cookiee.cookieeserver.constant.StatusCode;
import com.cookiee.cookieeserver.domain.Category;
import com.cookiee.cookieeserver.domain.User;
import com.cookiee.cookieeserver.dto.BaseResponseDto;
import com.cookiee.cookieeserver.dto.DataResponseDto;
import com.cookiee.cookieeserver.dto.ErrorResponseDto;
import com.cookiee.cookieeserver.dto.request.CategoryCreateRequestDto;
import com.cookiee.cookieeserver.repository.CategoryRepository;
import com.cookiee.cookieeserver.service.CategoryService;
import com.cookiee.cookieeserver.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final UserService userService;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    // 카테고리 등록
    @PostMapping("/category/{userId}")
    public BaseResponseDto<Category> postCategory(@PathVariable int userId,
                                                  @RequestBody CategoryCreateRequestDto requestDto){
        Category category;
        try {
            Optional<User> user = userService.findOneById(userId);
            if(user.isEmpty()){
                return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "해당 id의 사용자가 존재하지 않습니다.");
            }
            else{
                category = categoryService.create(user.get(), requestDto);
            }
        }
        catch (Exception e){
            return ErrorResponseDto.of(StatusCode.BAD_REQUEST, "카테고리 등록에 실패하였습니다.");
        }
        return DataResponseDto.of(category, "카테고리 등록에 성공하였습니다.");
    }

    // 카테고리 조회
//    @GetMapping("/category/{userId}")
//    public BaseResponseDto<Category> getCategory(@PathVariable int userId){
//        Optional<Category> category;
//
//
//    }

    // 카테고리 수정

    // 카테고리 삭제
}
