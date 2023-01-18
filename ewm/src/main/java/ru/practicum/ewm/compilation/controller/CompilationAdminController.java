package ru.practicum.ewm.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.service.CompilationAdminService;
import ru.practicum.ewm.utility.marker.Create;

import javax.validation.constraints.NotNull;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/compilations")
public class CompilationAdminController {

    private final CompilationAdminService compilationAdminService;
    private final String changeCompilationUrlPart = "/{compId}/events/{eventId}";
    private final String pinCompilationUrlPart = "/{compId}/pin";

    // Добавление новой подборки
    @PostMapping
    public CompilationDto addNewCompilation(
            @Validated(Create.class) @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Добавление новой подборки {}", newCompilationDto);
        return compilationAdminService.addNewCompilation(newCompilationDto);
    }

    // Удаление подборки
    @DeleteMapping("/{compId}")
    public void deleteCompilation(@NotNull @PathVariable Long compId) {
        log.info("Удаление подборки  с id={}", compId);
        compilationAdminService.deleteCompilation(compId);
    }

    // Удалить событие из подборки
    @DeleteMapping(changeCompilationUrlPart)
    public void deleteEventFromCompilation(
            @NotNull @PathVariable Long compId,
            @NotNull @PathVariable Long eventId) {
        log.info("Удаление события с id={} из подборки с id={}", eventId, compId);
        compilationAdminService.deleteEventFromCompilation(compId, eventId);
    }

    // Добавить событие в подборку
    @PatchMapping(changeCompilationUrlPart)
    public void addEventToCompilation(
            @NotNull @PathVariable Long compId,
            @NotNull @PathVariable Long eventId) {
        log.info("Добавление события с id={} в подборку с id={}", eventId, compId);
        compilationAdminService.addEventToCompilation(compId, eventId);
    }

    // Открепить подборку от главной страницы
    @DeleteMapping(pinCompilationUrlPart)
    public void unpinCompilation(@NotNull @PathVariable Long compId) {
        log.info("Открепление подборки с id={} от главной страницы", compId);
        compilationAdminService.pinOrUnpinCompilation(compId, false);
    }

    // Закрепить подборку на главной странице
    @PatchMapping(pinCompilationUrlPart)
    public void pinCompilation(@NotNull @PathVariable Long compId) {
        log.info("Закрепление подборки с id={} на главной странице", compId);
        compilationAdminService.pinOrUnpinCompilation(compId, true);
    }
}
