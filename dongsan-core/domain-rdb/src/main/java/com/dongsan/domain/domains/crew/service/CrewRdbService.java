package com.dongsan.domain.domains.crew.service;

import com.dongsan.domain.domains.crew.domain.*;
import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
import com.dongsan.domain.support.paging.CursorResponse;
import org.springframework.stereotype.Service;

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

    public Crew getCrewWithLock(Long crewId) {
        return crewRepository.findByIdWithLock(crewId)
                .orElseThrow(() -> new CoreException(CoreErrorCode.CREW_NOT_FOUND));
    }

    public boolean isNameDuplicated(String name) {
        return crewRepository.existsByName(name);
    }

    public Long save(CrewInfoCommand command) {
        if (isNameDuplicated(command.name())) {
            throw new CoreException(CoreErrorCode.CREW_NAME_DUPLICATED);
        }

        CrewInfo info = new CrewInfo(command.name(), command.description(), command.rule(), command.crewImageUrl());
        Capacity capacity = new Capacity(command.limitEnable(), command.memberLimit());
        CrewAccessPolicy accessPolicy = switch (command.exposeLevel()) {
            case PUBLIC -> CrewAccessPolicy.publicCrew();
            case PRIVATE -> {
                String hashedPassword = passwordHasher.hash(command.password());
                yield CrewAccessPolicy.privateCrew(hashedPassword);
            }
        };
        Crew crew = new Crew(info, capacity, accessPolicy);
        return crewRepository.save(crew);
    }

    public void update(Crew crew, CrewInfoCommand command) {
        if (isNameDuplicated(command.name())) {
            throw new CoreException(CoreErrorCode.CREW_NAME_DUPLICATED);
        }

        CrewInfo info = new CrewInfo(command.name(), command.description(), command.rule(), command.crewImageUrl());
        Capacity capacity = new Capacity(command.limitEnable(), command.memberLimit());
        CrewAccessPolicy accessPolicy = switch (command.exposeLevel()) {
            case PUBLIC -> CrewAccessPolicy.publicCrew();
            case PRIVATE -> {
                String hashedPassword = passwordHasher.hash(command.password());
                yield CrewAccessPolicy.privateCrew(hashedPassword);
            }
        };
        crew.update(info, capacity, accessPolicy);
    }

    public void comparePassword(Crew crew, String password) {
        if (!crew.needsPassword()) {
            return;
        }
        boolean verified = passwordHasher.verify(password.trim(), crew.getPassword());
        if (!verified) {
            throw new CoreException(CoreErrorCode.CREW_PASSWORD_INVALID);
        }
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
