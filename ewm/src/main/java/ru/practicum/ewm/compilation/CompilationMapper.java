package ru.practicum.ewm.compilation;

import org.springframework.stereotype.Component;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.event.common.EventMapper;
import ru.practicum.ewm.event.model.Event;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CompilationMapper {

    public static CompilationDto compilationToCompilationDto(Compilation compilation) {
        return new CompilationDto(
                compilation.getId(),
                compilation.getEvents().stream()
                        .map(EventMapper::eventToEventShortDto)
                        .collect(Collectors.toSet()),
                compilation.getPinned(),
                compilation.getTitle()
        );
    }

    public static Compilation compilationDtoToCompilation(NewCompilationDto newCompilationDto, Set<Event> events) {
        return new Compilation(
                null,
                events,
                newCompilationDto.getPinned(),
                newCompilationDto.getTitle()
        );
    }
}
