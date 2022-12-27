package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.dto.NewCategoryDto;
import ru.practicum.ewm.category.service.CategoryAdminService;
import ru.practicum.ewm.utility.marker.Create;
import ru.practicum.ewm.utility.marker.Update;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/categories")
public class CategoryAdminController {

    private final CategoryAdminService categoryService;

    // Admin: Categories - Изменение категории
    @PatchMapping
    public CategoryDto changeCategory(@Validated(Update.class) @RequestBody CategoryDto categoryDto) {
        log.info("Изменение категории с id={}", categoryDto.getId());
        return categoryService.changeCategory(categoryDto);
    }

    // Admin: Categories - Добавление новой категории
    @PostMapping
    public CategoryDto addNewCategory(@Validated(Create.class) @RequestBody NewCategoryDto newCategoryDto) {
        CategoryDto categoryDto = categoryService.addNewCategory(newCategoryDto);

        log.info("Добавление новой категории {}", categoryDto);
        return categoryDto;
    }

    // Admin: Categories - Удаление категории
    @DeleteMapping("/{catId}")
    public void deleteCategory(@PathVariable Long catId) {
        log.info("Удаление категории c id={}", catId);
        categoryService.deleteCategory(catId);
    }
}
