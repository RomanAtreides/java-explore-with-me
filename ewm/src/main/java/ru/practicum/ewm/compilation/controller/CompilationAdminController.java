package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
