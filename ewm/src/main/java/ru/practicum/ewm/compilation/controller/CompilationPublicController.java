package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.service.CompilationPublicService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/compilations")
public class CompilationPublicController {

    private final CompilationPublicService compilationPublicService;

    // Получение подборок событий
    @GetMapping
    public List<CompilationDto> findCompilations(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(required = false, defaultValue = "0") Integer from,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Получение подборок событий from={}, size={} с параметром pinned={}", from, size, pinned);
        return compilationPublicService.findCompilations(pinned, from, size);
    }

    // Получение подборки событий по её идентификатору
    @GetMapping("/{compId}")
    public CompilationDto findCompilationById(@PathVariable Long compId) {
        log.info("Получение подборки событий с id={}", compId);
        return compilationPublicService.findCompilationById(compId);
    }
}
