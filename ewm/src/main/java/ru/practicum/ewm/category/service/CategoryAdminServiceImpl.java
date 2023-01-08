package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.CategoryMapper;
import ru.practicum.ewm.category.CategoryValidator;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryAdminServiceImpl implements CategoryAdminService {

    private final CategoryRepository categoryRepository;
    private final CategoryValidator categoryValidator;

    @Override
    public CategoryDto changeCategory(CategoryDto categoryDto) {
        Long catId = categoryDto.getId();
        Category category = categoryValidator.getCategoryIfExists(catId);

        category.setId(catId);
        category.setName(categoryDto.getName());
        return CategoryMapper.toCategoryDto(category);
    }

    @Override
    public CategoryDto addNewCategory(NewCategoryDto newCategoryDto) {
        final Category category = CategoryMapper.newCategoryDtoToCategory(newCategoryDto);
        final Category entity = categoryRepository.save(category);

        return CategoryMapper.toCategoryDto(entity);
    }

    @Override
    public void deleteCategory(Long catId) {
        Category category = categoryValidator.getCategoryIfExists(catId);

        categoryRepository.delete(category);
    }
}
