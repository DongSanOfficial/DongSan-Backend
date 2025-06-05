package com.dongsan.domain.domains.crew.service;

import org.springframework.stereotype.Service;

import com.dongsan.domain.domains.crew.domain.Capacity;
import com.dongsan.domain.domains.crew.domain.Crew;
import com.dongsan.domain.domains.crew.domain.CrewRepository;
import com.dongsan.domain.domains.crew.domain.PrivateCrew;
import com.dongsan.domain.domains.crew.domain.PublicCrew;
import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import com.dongsan.domain.support.paging.CursorResponse;

@Service
public class CrewRdbService {
    private final PasswordHasher passwordHasher;
    private final CrewRepository crewRepository;

    public CrewRdbService(PasswordHasher passwordHasher, CrewRepository crewRepository) {
        this.passwordHasher = passwordHasher;
        this.crewRepository = crewRepository;
    }

    public Crew getCrew(Long crewId) {
        return crewRepository.findById(crewId)
                .orElseThrow(() -> new CoreException(CoreErrorCode.CREW_NOT_FOUND));
    }

    public boolean isNameDuplicated(String name) {
        return crewRepository.existsByName(name);
    }

    public Long save(CreateCrewCommand command) {
        if (isNameDuplicated(command.name())) {
            throw new CoreException(CoreErrorCode.CREW_NAME_DUPLICATED);
        }

        Capacity capacity = new Capacity(command.limitEnable(), command.memberLimit());
        Crew crew = switch (command.exposeLevel()) {
            case PUBLIC -> new PublicCrew(command.name(), command.description(), command.rule(), command.crewImageUrl(),
                    capacity);
            case PRIVATE -> {
                String hashedPassword = passwordHasher.hash(command.password());
                yield new PrivateCrew(command.name(), command.description(), command.rule(), command.crewImageUrl(),
                        capacity, hashedPassword);
            }
        };

        return crewRepository.save(crew);
    }

    public CursorResponse<Crew> getMyCrews(Long memberId, Integer size, Long lastId) {
        return crewRepository.getMyCrews(memberId, size, lastId);
    }

    public CursorResponse<Crew> searchCrews(String name, Integer size, Long lastId) {
        return crewRepository.searchCrews(name, size, lastId);
    }

    public CursorResponse<Crew> recommendCrews(Integer size, Long lastId) {
        return crewRepository.findCrewsByLogThisWeek(size, lastId);
    }
}
