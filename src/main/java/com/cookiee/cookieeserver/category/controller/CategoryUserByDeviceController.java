package com.cookiee.cookieeserver.category.controller;

import com.cookiee.cookieeserver.category.domain.Category;
import com.cookiee.cookieeserver.category.service.CategoryUserByDeviceService;
import com.cookiee.cookieeserver.login.jwt.JwtService;
import com.cookiee.cookieeserver.global.dto.BaseResponseDto;
import com.cookiee.cookieeserver.category.dto.request.CategoryCreateRequestDto;
import com.cookiee.cookieeserver.category.dto.request.CategoryUpdateRequestDto;
import com.cookiee.cookieeserver.category.dto.response.CategoryResponseDto;
import com.cookiee.cookieeserver.event.dto.response.EventCategoryGetResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.cookiee.cookieeserver.global.SuccessCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="api/v1/categories/")
@Tag(name="카테고리 CRUD", description="(디바이스 등록 유저용) 카테고리를 등록/조회/수정/삭제 할 수 있습니다.")
public class CategoryUserByDeviceController {
    private final CategoryUserByDeviceService categoryUserByDeviceService;
    private final JwtService jwtService;

    /***
     * 카테고리 등록
     * @param deviceId        디바이스 아이디
     * @param requestDto    등록하려는 카테고리 내용
     * @return              CategoryResponseDto
     */

    @PostMapping("{deviceId}")
    @Operation(summary = "카테고리 등록")
    public BaseResponseDto<CategoryResponseDto> postCategoryUserByDevice(@PathVariable String deviceId,
                                                             @RequestBody CategoryCreateRequestDto requestDto){
        Category newCategory = categoryUserByDeviceService.create(deviceId, requestDto);
        CategoryResponseDto categoryResponseDto = CategoryResponseDto.builder()
                .categoryId(newCategory.getCategoryId())
                .categoryName(newCategory.getCategoryName())
                .categoryColor(newCategory.getCategoryColor())
                .build();
        return BaseResponseDto.ofSuccess(CREATE_CATEGORY_SUCCESS, categoryResponseDto);
    }

    @GetMapping("{deviceId}")
    @Operation(summary = "카테고리 조회")
    public BaseResponseDto<List<CategoryResponseDto>> getCategoryUserByDevice(@PathVariable String deviceId){
        List<CategoryResponseDto> result = categoryUserByDeviceService.getAllCategories(deviceId);
        return BaseResponseDto.ofSuccess(GET_CATEGORY_SUCCESS, result);
    }

    @PutMapping("{deviceId}/{categoryId}")
    @Operation(summary = "카테고리 수정")
    public BaseResponseDto<CategoryResponseDto> updateCategoryUserByDevice(@PathVariable String deviceId,
                                                               @PathVariable Long categoryId,
                                                               @RequestBody CategoryUpdateRequestDto requestDto){
        CategoryResponseDto result = categoryUserByDeviceService.update(deviceId, categoryId, requestDto);
        return BaseResponseDto.ofSuccess(MODIFY_CATEGORY_SUCCESS, result);
    }

    @DeleteMapping("{deviceId}/{categoryId}")
    @Operation(summary = "카테고리 삭제")
    public BaseResponseDto<?> deleteCategoryUserByDevice(@PathVariable String deviceId,
                                             @PathVariable Long categoryId){
        categoryUserByDeviceService.delete(deviceId, categoryId);
        return BaseResponseDto.ofSuccess(DELETE_CATEGORY_SUCCESS);
    }

    @GetMapping("collection/{deviceId}/{categoryId}")
    @Operation(summary = "카테고리 모아보기")
    public BaseResponseDto<EventCategoryGetResponseDto> getCollectionUserByDevice(@PathVariable String deviceId,
                                                                      @PathVariable Long categoryId){
        EventCategoryGetResponseDto result = categoryUserByDeviceService.findByIdForCollection(deviceId, categoryId);
        return BaseResponseDto.ofSuccess(GET_COLLECTION_SUCCESS, result);
    }
}
