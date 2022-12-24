package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryDto;

import java.util.List;

public interface CategoryPublicService {

    List<CategoryDto> findCategories(Integer from, Integer size);

    CategoryDto findCategoryById(Long catId);
}
