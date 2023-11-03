package com.cookiee.cookieeserver.service;

import com.cookiee.cookieeserver.domain.Category;
import com.cookiee.cookieeserver.domain.User;
import com.cookiee.cookieeserver.dto.request.CategoryCreateRequestDto;
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
        // 중복 검사 필요함
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
}