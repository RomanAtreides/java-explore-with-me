package ru.practicum.ewm.category.service;

import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;

public interface CategoryAdminService {

    CategoryDto changeCategory(CategoryDto categoryDto);

    CategoryDto addNewCategory(NewCategoryDto newCategoryDto);

    void deleteCategory(Long catId);
}
