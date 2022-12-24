package ru.practicum.ewm.category.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.category.CategoryMapper;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.utility.FromSizeRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryPublicServiceImpl implements CategoryPublicService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> findCategories(Integer from, Integer size) {
        Pageable pageable = FromSizeRequest.of(from, size);

        return categoryRepository.findAll(pageable).map(CategoryMapper::toCategoryDto).getContent();
    }

    @Override
    public CategoryDto findCategoryById(Long catId) {
        Category category = categoryRepository.findById(catId)
                .orElseThrow(() -> new RuntimeException("category not found!"));
        return CategoryMapper.toCategoryDto(category);
    }
}
