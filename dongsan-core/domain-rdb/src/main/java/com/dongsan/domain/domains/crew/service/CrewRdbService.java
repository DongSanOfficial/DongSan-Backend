package com.dongsan.domain.domains.crew.service;

import com.dongsan.domain.domains.crew.domain.*;
import com.dongsan.domain.support.error.CoreErrorCode;
import com.dongsan.domain.support.error.CoreException;
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

    public Long save(CreateCrewCommand command) {
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

//    public void updateCrewInfo(Crew crew, String name, String description, String rule, String imageUrl) {
//        if (isNameDuplicated(name)) {
//            throw new CoreException(CoreErrorCode.CREW_NAME_DUPLICATED);
//        }
//
//        crew.updateCrewInfo(name, description, rule, imageUrl);
//
//        // 비공개 크루 일 때만 비밀번호 수정
//        if(crew.needsPassword()){
//            String hashedPassword = passwordHasher.hash(password);
//            (PrivateCrew) crew.updatePassword(hashedPassword);
//        }
//
//        // 이미 가입되어 있는 인원보다 적게 수정할 수 있는지.
//        int memberCount = ;
//        if(memberCount > inputMemberLimit){
//            throw new CoreException();
//        }
//
//        Capacity capacity = new Capacity(command.limitEnable(), command.memberLimit());
//        crew.updateCapacity(capacity);
//    }

    public void comparePassword(Crew crew, String password) {
        if (!crew.needsPassword()) {
            return;
        }
        boolean verified = passwordHasher.verify(password.trim(), crew.getPassword());
        if (!verified) {
            throw new CoreException(CoreErrorCode.CREW_PASSWORD_INVALID);
        }
    }
}
