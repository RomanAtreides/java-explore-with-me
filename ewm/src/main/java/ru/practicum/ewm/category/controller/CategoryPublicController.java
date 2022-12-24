package ru.practicum.ewm.category.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.category.dto.CategoryDto;
import ru.practicum.ewm.category.service.CategoryPublicService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryPublicController {

    private final CategoryPublicService categoryPublicService;

    // Public: Categories - Получение категорий
    @GetMapping
    public List<CategoryDto> findCategories(
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Получение категорий from={}, size={}", from, size);
        return categoryPublicService.findCategories(from, size);
    }

    // Public: Categories - Получение информации о категории по её идентификатору
    @GetMapping("/{catId}")
    public CategoryDto findCategoryById(@PathVariable Long catId) {
        log.info("Получение информации о категории с id={}", catId);
        return categoryPublicService.findCategoryById(catId);
    }
}
