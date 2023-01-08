package ru.practicum.ewm.compilation.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.exception.EntityNotFoundException;
import ru.practicum.ewm.exception.ValidationException;

@Component
@RequiredArgsConstructor
public class CompilationValidator {

    private final CompilationRepository compilationRepository;

    public Compilation findCompilationIfExists(Long compId) {
        String exceptionMessage = String.format("Подборка с id=%d не найдена", compId);

        if (compId == null) {
            throw new ValidationException(exceptionMessage);
        }
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new EntityNotFoundException(String.format(exceptionMessage)));
    }
}
