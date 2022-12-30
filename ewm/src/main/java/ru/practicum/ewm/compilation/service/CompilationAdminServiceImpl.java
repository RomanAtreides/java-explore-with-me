package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.CompilationMapper;
import ru.practicum.ewm.compilation.CompilationValidator;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class CompilationAdminServiceImpl implements CompilationAdminService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    private final CompilationValidator compilationValidator;

    @Override
    public CompilationDto addNewCompilation(NewCompilationDto newCompilationDto) {
        Set<Long> eventIds = newCompilationDto.getEvents();
        Set<Event> events = eventRepository.findEventsByIds(eventIds);
        final Compilation compilation = CompilationMapper.compilationDtoToCompilation(newCompilationDto, events);
        final Compilation entity = compilationRepository.save(compilation);

        return CompilationMapper.compilationToCompilationDto(entity);
    }

    @Override
    public void deleteCompilation(Long compId) {
        Compilation compilation = compilationValidator.findCompilationIfExists(compId);
        compilationRepository.delete(compilation);
    }

    @Override
    public void deleteEventFromCompilation(Long compId, Long eventId) {
        eventRepository.deleteById(eventId); // TODO: 30.12.2022 Закончить метод
    }

    @Override
    public void addEventToCompilation(Long compId, Long eventId) {
        // TODO: 30.12.2022 Закончить метод
    }

    @Override
    public void pinOrUnpinCompilation(Long compId, boolean isPinned) {
        Compilation compilation = compilationValidator.findCompilationIfExists(compId);
        compilation.setPinned(isPinned);
        compilationRepository.save(compilation);
    }
}
