package ru.practicum.ewm.compilation.service;

import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;

public interface CompilationAdminService {
    CompilationDto addNewCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(Long compId);
}
