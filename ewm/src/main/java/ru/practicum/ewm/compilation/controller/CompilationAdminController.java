package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.service.CompilationAdminService;
import ru.practicum.ewm.utility.marker.Create;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/compilations")
public class CompilationAdminController {

    private final CompilationAdminService compilationAdminService;

    // Добавление новой подборки
    @PostMapping
    public CompilationDto addNewCompilation(
            @Validated(Create.class) @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Добавление новой подборки {}", newCompilationDto);
        return compilationAdminService.addNewCompilation(newCompilationDto);
    }

    // Удаление подборки
    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Удаление подборки  с id={}", compId);
        compilationAdminService.deleteCompilation(compId);
    }
}
