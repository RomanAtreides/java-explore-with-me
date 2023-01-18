package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.common.CompilationMapper;
import ru.practicum.ewm.compilation.common.CompilationValidator;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.dto.NewCompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.event.repository.EventRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class CompilationAdminServiceImpl implements CompilationAdminService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final CompilationValidator compilationValidator;
    private final EntityManager entityManager;

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
        final Compilation compilation = compilationValidator.findCompilationIfExists(compId);

        compilationRepository.delete(compilation);
    }

    @Override
    public void deleteEventFromCompilation(Long compId, Long eventId) {
        Query query = entityManager.createNativeQuery(
                "DELETE FROM compilation_of_events WHERE compilation_id = ?1 AND event_id = ?2"
        );

        query.setParameter(1, compId).setParameter(2, eventId).executeUpdate();
    }

    @Override
    public void addEventToCompilation(Long compId, Long eventId) {
        Query query = entityManager.createNativeQuery(
                "INSERT INTO compilation_of_events (compilation_id, event_id) VALUES (?1, ?2)"
        );

        query.setParameter(1, compId).setParameter(2, eventId).executeUpdate();
    }

    @Override
    public void pinOrUnpinCompilation(Long compId, boolean isPinned) {
        final Compilation compilation = compilationValidator.findCompilationIfExists(compId);

        compilation.setPinned(isPinned);
        compilationRepository.save(compilation);
    }
}
