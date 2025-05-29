package com.dongsan.domain.domains.crew.service;

import com.dongsan.domain.domains.crew.domain.CrewRepository;
import org.springframework.stereotype.Service;

@Service
public class CrewRdbService {
    private final CrewRepository crewRepository;

    public CrewRdbService(CrewRepository crewRepository) {
        this.crewRepository = crewRepository;
    }

    public boolean isNameDuplicated(String name) {
        return crewRepository.existsByName(name);
    }
}
