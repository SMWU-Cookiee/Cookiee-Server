package com.cookiee.cookieeserver.service;

import com.cookiee.cookieeserver.domain.Category;
import com.cookiee.cookieeserver.domain.User;
import com.cookiee.cookieeserver.dto.request.CategoryCreateRequestDto;
import com.cookiee.cookieeserver.dto.request.CategoryUpdateRequestDto;
import com.cookiee.cookieeserver.dto.response.CategoryGetResponseDto;
import com.cookiee.cookieeserver.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    // 카테고리 생성
    @Transactional
    public Category create(User user, CategoryCreateRequestDto requestDto) {
        // 중복 검사
        if(categoryRepository.existsByCategoryName(requestDto.getCategoryName())
                || categoryRepository.existsByCategoryColor(requestDto.getCategoryColor())){
            throw new IllegalArgumentException("카테고리 등록에 실패하였습니다. 중복되는 카테고리입니다.");

        }

        return categoryRepository.save(requestDto.toEntity(user));
    }

    @Transactional
    public List<CategoryGetResponseDto> getAllCategories(int userId) {
        return categoryRepository.findCategoriesByUserUserId(userId);
    }

//    @Override
//    public List<CategoryGetResponseDto> getAllCategories(int userId) {
//        List<CategoryGetResponseDto> categoryList = categoryRepository.findCategoriesByUserId(userId);
//
//        return categoryList;
//    }

    @Transactional
    public Category update(int categoryId, CategoryUpdateRequestDto requestDto){
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new IllegalArgumentException(("해당 id의 카테고리가 존재하지 않습니다."))
        );

        category.update(requestDto.getCategoryName(), requestDto.getCategoryColor());

        return category;
    }
}