package com.cookiee.cookieeserver.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.cookiee.cookieeserver.domain.Category;
import com.cookiee.cookieeserver.domain.Event;
import com.cookiee.cookieeserver.domain.EventCategory;
import com.cookiee.cookieeserver.domain.User;
import com.cookiee.cookieeserver.dto.request.CategoryCreateRequestDto;
import com.cookiee.cookieeserver.dto.request.CategoryUpdateRequestDto;
import com.cookiee.cookieeserver.dto.response.CategoryResponseDto;
import com.cookiee.cookieeserver.dto.response.EventCategoryGetResponseDto;
import com.cookiee.cookieeserver.repository.CategoryRepository;
import com.cookiee.cookieeserver.repository.EventCategoryRepository;
import com.cookiee.cookieeserver.repository.EventRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventCategoryRepository eventCategoryRepository;

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
    public EventCategoryGetResponseDto findByIdForCollection(Long id){
        Category categoryEntity = categoryRepository.findByCategoryId(id).orElseThrow(
                () -> new IllegalArgumentException("해당 아이디로 카테고리를 찾을 수 없습니다.")
        );

        List<EventCategory> eventCategory = eventCategoryRepository.findByCategoryId(categoryEntity);

        List<Event> eventList = categoryEntity.getEventCategories().stream()
                .map(EventCategory::getEvent)
                .collect(Collectors.toList());

//        List<Event> eventList = eventCategories.stream()
//                .map(EventCategory::getEvent)
//                .collect(Collectors.toList());

        return EventCategoryGetResponseDto.builder()
                .category(categoryEntity)
                .eventList(eventList)
                .build();
    }

    @Transactional
    public List<CategoryResponseDto> getAllCategories(int userId) {
        return categoryRepository.findCategoriesByUserUserId(userId);
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
    public CategoryResponseDto update(int userId, Long categoryId, CategoryUpdateRequestDto requestDto){
        Category category = categoryRepository.findByCategoryId(categoryId).orElseThrow(() ->
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
    public void delete(int userId, Long categoryId) {
        // 카테고리 아이디 존재 유무 확인
        Category category = categoryRepository.findByCategoryId(categoryId).orElseThrow(() ->
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