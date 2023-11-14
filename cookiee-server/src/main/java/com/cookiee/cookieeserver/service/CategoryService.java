package com.cookiee.cookieeserver.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.cookiee.cookieeserver.domain.Category;
import com.cookiee.cookieeserver.domain.User;
import com.cookiee.cookieeserver.dto.request.CategoryCreateRequestDto;
import com.cookiee.cookieeserver.dto.request.CategoryUpdateRequestDto;
import com.cookiee.cookieeserver.dto.response.CategoryResponseDto;
import com.cookiee.cookieeserver.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<CategoryResponseDto> getAllCategories(int userId) {
        return categoryRepository.findCategoriesByUserUserId(userId);
    }

//    @Override
//    public List<CategoryResponseDto> getAllCategories(int userId) {
//        List<CategoryResponseDto> categoryList = categoryRepository.findCategoriesByUserId(userId);
//
//        return categoryList;
//    }

    @Transactional
    public CategoryResponseDto update(int userId, int categoryId, CategoryUpdateRequestDto requestDto){
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException(("해당 id의 카테고리가 존재하지 않습니다."))
        );

        // 유저 아이디와 카테고리 아이디가 부합하는지 확인
        if(categoryRepository.existsByCategoryIdInUser(userId, categoryId) == 1){
            category.update(requestDto.getCategoryName(), requestDto.getCategoryColor());
        }
        else{
            throw new NotFoundException("해당 유저에게 요청한 카테고리가 존재하지 않습니다.");
        }

        return new CategoryResponseDto(category.getCategoryId(), category.getCategoryName(), category.getCategoryColor());
    }

    @Transactional
    public void delete(int userId, int categoryId) {
        // 카테고리 아이디 존재 유무 확인
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new NotFoundException(("해당 id의 카테고리가 존재하지 않습니다."))
        );

        // 유저 아이디와 카테고리 아이디가 부합하는지 확인
        if (categoryRepository.existsByCategoryIdInUser(userId, categoryId) == 1) {
            categoryRepository.delete(category);
        } else {
            throw new NotFoundException("해당 유저에게 요청한 카테고리가 존재하지 않습니다.");
        }
    }
}