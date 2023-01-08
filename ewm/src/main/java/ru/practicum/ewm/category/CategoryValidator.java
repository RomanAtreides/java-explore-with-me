package ru.practicum.ewm.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.category.repository.CategoryRepository;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.exception.ValidationException;

@Component
@RequiredArgsConstructor
public class CategoryValidator {

    private final CategoryRepository categoryRepository;

    public Category getCategoryIfExists(Long catId) {
        String exceptionMessage = String.format("Категория с id=%d не найдена", catId);

        if (catId == null) {
            throw new ValidationException(exceptionMessage);
        }
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new EntityNotFoundException(exceptionMessage));
    }
}
