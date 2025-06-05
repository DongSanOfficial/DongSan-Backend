package fixture;

import com.dongsan.domain.domains.crew.domain.Capacity;
import com.dongsan.domain.domains.crew.domain.Crew;
import com.dongsan.domain.domains.crew.domain.CrewAccessPolicy;
import com.dongsan.domain.domains.crew.domain.CrewInfo;

public class CrewTestBuilder {
    private String name = "test crew name";
    private String description = "test crew description";
    private String rule = "test crew rule";
    private String crewImageUrl = "test image";
    private Capacity capacity = new Capacity(true, 10);
    private CrewAccessPolicy crewAccessPolicy = CrewAccessPolicy.publicCrew();

    public CrewTestBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CrewTestBuilder description(String description) {
        this.description = description;
        return this;
    }

    public CrewTestBuilder rule(String rule) {
        this.rule = rule;
        return this;
    }

    public CrewTestBuilder crewImageUrl(String crewImageUrl) {
        this.crewImageUrl = crewImageUrl;
        return this;
    }

    public CrewTestBuilder capacity(Capacity capacity) {
        this.capacity = capacity;
        return this;
    }

    public CrewTestBuilder publicCrew() {
        this.crewAccessPolicy = CrewAccessPolicy.publicCrew();
        return this;
    }

    public CrewTestBuilder privateCrew(String hashedPassword) {
        this.crewAccessPolicy = CrewAccessPolicy.privateCrew(hashedPassword);
        return this;
    }

    public Crew build() {
        CrewInfo info = new CrewInfo(name, description, rule, crewImageUrl);
        return new Crew(info, capacity, crewAccessPolicy);
    }
}
