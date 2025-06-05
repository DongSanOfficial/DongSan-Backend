package fixture;

import com.dongsan.domain.domains.crew.domain.CrewInfo;

public class CrewInfoTestBuilder {
    private String name = "test crew name";
    private String description = "test crew description";
    private String rule = "test crew rule";
    private String crewImageUrl = "test image";

    public CrewInfoTestBuilder name(String name) {
        this.name = name;
        return this;
    }

    public CrewInfoTestBuilder description(String description) {
        this.description = description;
        return this;
    }

    public CrewInfoTestBuilder rule(String rule) {
        this.rule = rule;
        return this;
    }

    public CrewInfoTestBuilder crewImageUrl(String crewImageUrl) {
        this.crewImageUrl = crewImageUrl;
        return this;
    }

    public CrewInfo build() {
        return new CrewInfo(name, description, rule, crewImageUrl);
    }
}
