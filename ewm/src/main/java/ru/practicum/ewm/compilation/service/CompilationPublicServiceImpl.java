package ru.practicum.ewm.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.compilation.CompilationMapper;
import ru.practicum.ewm.compilation.CompilationValidator;
import ru.practicum.ewm.compilation.dto.CompilationDto;
import ru.practicum.ewm.compilation.model.Compilation;
import ru.practicum.ewm.compilation.repository.CompilationRepository;
import ru.practicum.ewm.utility.FromSizeRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompilationPublicServiceImpl implements CompilationPublicService {

    private final CompilationRepository compilationRepository;
    private final CompilationValidator compilationValidator;

    @Override
    public List<CompilationDto> findCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = FromSizeRequest.of(from, size);

        List<Compilation> compilations = pinned != null ?
                compilationRepository.findCompilations(pinned, pageable) :
                compilationRepository.findAll(pageable).getContent();

        return compilations.stream()
                .map(CompilationMapper::compilationToCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto findCompilationById(Long compId) {
        Compilation compilation = compilationValidator.findCompilationIfExists(compId);

        return CompilationMapper.compilationToCompilationDto(compilation);
    }
}
