package com.cookiee.cookieeserver.category.service;

import com.cookiee.cookieeserver.category.domain.Category;
import com.cookiee.cookieeserver.event.domain.Event;
import com.cookiee.cookieeserver.global.domain.EventCategory;
import com.cookiee.cookieeserver.global.exception.GeneralException;
import com.cookiee.cookieeserver.user.domain.UserV2;
import com.cookiee.cookieeserver.category.dto.request.CategoryCreateRequestDto;
import com.cookiee.cookieeserver.category.dto.request.CategoryUpdateRequestDto;
import com.cookiee.cookieeserver.category.dto.response.CategoryResponseDto;
import com.cookiee.cookieeserver.event.dto.response.EventCategoryGetResponseDto;
import com.cookiee.cookieeserver.category.repository.CategoryRepository;
import com.cookiee.cookieeserver.global.repository.EventCategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.cookiee.cookieeserver.global.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class CategoryServiceV2 {
    private final CategoryRepository categoryRepository;
    private final EventCategoryRepository eventCategoryRepository;

    // 카테고리 생성
    @Transactional
    public Category create(UserV2 userV2, CategoryCreateRequestDto requestDto) {
        // 이름만 중복 검사
        if(categoryRepository.existsByCategoryColorAndUserUserId(requestDto.getCategoryColor(), userV2.getUserId())){
            throw new GeneralException(CATEGORY_NAME_EXISTS);
        }

        return categoryRepository.save(requestDto.toEntity(userV2));
    }

    @Transactional
    public EventCategoryGetResponseDto findByIdForCollection(UserV2 userV2, Long categoryId){
        Category categoryEntity = categoryRepository.findByUserUserIdAndCategoryId(userV2.getUserId(), categoryId)
                .orElseThrow(
                        () -> new GeneralException(CATEGORY_NOT_FOUND)
                );

        List<Event> eventList = categoryEntity.getEventCategories().stream()
                .map(EventCategory::getEvent)
                .collect(Collectors.toList());

        return EventCategoryGetResponseDto.builder()
                .category(categoryEntity)
                .eventList(eventList)
                .build();
    }

    @Transactional
    public List<CategoryResponseDto> getAllCategories(UserV2 userV2) {
        List<CategoryResponseDto> list = categoryRepository.findCategoriesByUserUserId(userV2.getUserId());
        return list;
    }

//    @Transactional
//    public Category getCategoryById(int categoryId){
//        return categoryRepository.findByCategoryId(categoryId);
//    }

    public Optional<Category> findOneById(Long categoryId) {
        return categoryRepository.findByCategoryId(categoryId);
    }

//    @Override
//    public List<CategoryResponseDto> getAllCategories(int userId) {
//        List<CategoryResponseDto> categoryList = categoryRepository.findCategoriesByUserId(userId);
//
//        return categoryList;
//    }

    @Transactional
    public CategoryResponseDto update(UserV2 userV2, Long categoryId, CategoryUpdateRequestDto requestDto){
        Long userId = userV2.getUserId();

        // 유저 아이디와 카테고리 아이디가 부합하는지 확인
        Category category = categoryRepository
                .findByUserUserIdAndCategoryId(userId, categoryId)
                .orElseThrow(() -> new GeneralException(CATEGORY_NOT_FOUND));

        // 카테고리 이름이 변경됐으면(같지 않으면) -> 중복 체크
        if(!category.getCategoryName().equals(requestDto.getCategoryName()) &
                categoryRepository.existsByCategoryNameAndUserUserId(requestDto.getCategoryName(), userId))
            throw new GeneralException(CATEGORY_NAME_EXISTS);
        else
            category.update(requestDto.getCategoryName(), requestDto.getCategoryColor());

        return new CategoryResponseDto(category.getCategoryId(), category.getCategoryName(), category.getCategoryColor());
    }

    @Transactional
    public void delete(UserV2 userV2, Long categoryId) {

        // 카테고리 아이디 존재 유무 확인
        Category category = categoryRepository.findByCategoryId(categoryId).orElseThrow(() ->
                new GeneralException(CATEGORY_NOT_FOUND)
        );

        // 유저 아이디와 카테고리 아이디가 부합하는지 확인
        if (categoryRepository.existsByCategoryIdInUser(userV2.getUserId(), categoryId) == 1) {
            // 외래키로 엮였기 때문에 해당 카테고리 아이디를 갖고 있는 EventCategory를 지워야 함
            eventCategoryRepository.deleteByCategoryCategoryId(categoryId);

            // 그 다음에 카테고리 삭제
            categoryRepository.delete(category);
        } else {
            throw new GeneralException(CATEGORY_NOT_FOUND);
        }
    }
}
