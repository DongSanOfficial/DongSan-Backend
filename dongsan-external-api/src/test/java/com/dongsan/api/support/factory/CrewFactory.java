package com.dongsan.api.support.factory;

import com.dongsan.domain.domains.crew.domain.*;
import com.dongsan.domain.domains.crew.service.PasswordHasher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;

@Component
public class CrewFactory {
    private static final String DEFAULT_NAME = "테스트 크루";
    private static final String DEFAULT_DESCRIPTION = "설명입니다";
    private static final String DEFAULT_RULE = "규칙입니다";
    private static final String DEFAULT_IMAGE_URL = "https://example.com/crew.png";
    private static final boolean DEFAULT_LIMIT_ENABLE = true;
    private static final int DEFAULT_MEMBER_LIMIT = 50;
    private static final String DEFAULT_PRIVATE_PASSWORD = "123456789";
    @Autowired
    DataSource dataSource;
    @Autowired
    private CrewRepository crewRepository;
    @Autowired
    private CrewMemberRepository crewMemberRepository;
    @Autowired
    private PasswordHasher passwordHasher;

    public Long save(CrewExposeLevel crewExposeLevel, Long memberId) {
        CrewInfo info = new CrewInfo(
                DEFAULT_NAME,
                DEFAULT_DESCRIPTION,
                DEFAULT_RULE,
                DEFAULT_IMAGE_URL
        );

        Capacity capacity = new Capacity(
                DEFAULT_LIMIT_ENABLE,
                DEFAULT_MEMBER_LIMIT
        );

        CrewAccessPolicy accessPolicy = switch (crewExposeLevel) {
            case PUBLIC -> CrewAccessPolicy.publicCrew();
            case PRIVATE -> {
                String hashed = passwordHasher.hash(DEFAULT_PRIVATE_PASSWORD);
                yield CrewAccessPolicy.privateCrew(hashed);
            }
        };

        Crew crew = new Crew(info, capacity, accessPolicy);
        crewRepository.save(crew);

        crewMemberRepository.save(new CrewMember(crew.getId(), memberId, CrewMemberRole.MANAGER));
        return crew.getId();
    }

    public Long save(String name, CrewExposeLevel crewExposeLevel, Long memberId) {
        CrewInfo info = new CrewInfo(
                name,
                DEFAULT_DESCRIPTION,
                DEFAULT_RULE,
                DEFAULT_IMAGE_URL
        );

        Capacity capacity = new Capacity(
                DEFAULT_LIMIT_ENABLE,
                DEFAULT_MEMBER_LIMIT
        );

        CrewAccessPolicy accessPolicy = switch (crewExposeLevel) {
            case PUBLIC -> CrewAccessPolicy.publicCrew();
            case PRIVATE -> {
                String hashed = passwordHasher.hash(DEFAULT_PRIVATE_PASSWORD);
                yield CrewAccessPolicy.privateCrew(hashed);
            }
        };

        Crew crew = new Crew(info, capacity, accessPolicy);
        crewRepository.save(crew);

        crewMemberRepository.save(new CrewMember(crew.getId(), memberId, CrewMemberRole.MANAGER));
        return crew.getId();
    }

    public void saveMember(Long memberId, Long crewId, CrewMemberRole crewMemberRole) {
        crewMemberRepository.save(new CrewMember(crewId, memberId, crewMemberRole));
    }

    public void saveMetaCrewRanking(Long crewId, Long logCount) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     """
                             INSERT 
                             INTO meta_crew_ranking (crew_id, log_count, created_at, updated_at) 
                             VALUES (?, ?, NOW(), NOW())
                             """)
        ) {
            ps.setLong(1, crewId);   // crew_id
            ps.setLong(2, logCount);   // log_count
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
