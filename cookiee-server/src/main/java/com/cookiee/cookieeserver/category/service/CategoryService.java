package com.cookiee.cookieeserver.category.service;

import com.amazonaws.services.kms.model.AlreadyExistsException;
import com.amazonaws.services.kms.model.NotFoundException;
import com.cookiee.cookieeserver.category.domain.Category;
import com.cookiee.cookieeserver.event.domain.Event;
import com.cookiee.cookieeserver.global.domain.EventCategory;
import com.cookiee.cookieeserver.user.domain.User;
import com.cookiee.cookieeserver.category.dto.request.CategoryCreateRequestDto;
import com.cookiee.cookieeserver.category.dto.request.CategoryUpdateRequestDto;
import com.cookiee.cookieeserver.category.dto.response.CategoryResponseDto;
import com.cookiee.cookieeserver.event.dto.response.EventCategoryGetResponseDto;
import com.cookiee.cookieeserver.category.repository.CategoryRepository;
import com.cookiee.cookieeserver.repository.EventCategoryRepository;
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
        if(categoryRepository.existsByCategoryColorAndUserUserId(requestDto.getCategoryName(), user.getUserId())
                || categoryRepository.existsByCategoryColorAndUserUserId(requestDto.getCategoryColor(), user.getUserId())){
            throw new IllegalArgumentException("카테고리 등록에 실패하였습니다. 중복되는 카테고리 이름 혹은 색상입니다.");

        }

        return categoryRepository.save(requestDto.toEntity(user));
    }

    @Transactional
    public EventCategoryGetResponseDto findByIdForCollection(Long id){
        Category categoryEntity = categoryRepository.findByCategoryId(id).orElseThrow(
                () -> new AlreadyExistsException("해당 아이디로 카테고리를 찾을 수 없습니다.")
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
    public List<CategoryResponseDto> getAllCategories(Long userId) {
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
    public CategoryResponseDto update(Long userId, Long categoryId, CategoryUpdateRequestDto requestDto){
        Category category = categoryRepository.findByCategoryId(categoryId).orElseThrow(() ->
                new NotFoundException(("해당 id의 카테고리가 존재하지 않습니다."))
        );

        // 유저 아이디와 카테고리 아이디가 부합하는지 확인
        category = categoryRepository
                .findByUserUserIdAndCategoryId(userId, categoryId)
                .orElse(null);

        if(category == null){
            throw new NotFoundException("해당 유저에게 요청한 카테고리가 존재하지 않습니다.");
        }
        else{
            if(categoryRepository.existsByCategoryNameAndUserUserId(requestDto.getCategoryName(), userId)
            || categoryRepository.existsByCategoryColorAndUserUserId(requestDto.getCategoryColor(), userId))
                throw new AlreadyExistsException("이미 존재하는 카테고리 이름 혹은 색상입니다.");
            else
                category.update(requestDto.getCategoryName(), requestDto.getCategoryColor());
        }
//        if(categoryRepository.existsByCategoryIdInUser(userId, categoryId) == 1){
//            category.update(requestDto.getCategoryName(), requestDto.getCategoryColor());
//        }
//        else{
//            throw new NotFoundException("해당 유저에게 요청한 카테고리가 존재하지 않습니다.");
//        }

        return new CategoryResponseDto(category.getCategoryId(), category.getCategoryName(), category.getCategoryColor());
    }

    @Transactional
    public void delete(Long userId, Long categoryId) {
        // 카테고리 아이디 존재 유무 확인
        Category category = categoryRepository.findByCategoryId(categoryId).orElseThrow(() ->
                new NotFoundException(("해당 id의 카테고리가 존재하지 않습니다."))
        );

        // 유저 아이디와 카테고리 아이디가 부합하는지 확인
        if (categoryRepository.existsByCategoryIdInUser(userId, categoryId) == 1) {
            // 외래키로 엮였기 때문에 해당 카테고리 아이디를 갖고 있는 EventCategory를 지워야 함
            eventCategoryRepository.deleteByCategoryCategoryId(categoryId);

            // 그 다음에 카테고리 삭제
            categoryRepository.delete(category);
        } else {
            throw new NotFoundException("해당 유저에게 요청한 카테고리가 존재하지 않습니다.");
        }
    }
}