package fixture;

import com.dongsan.domain.domains.crew.domain.Capacity;
import com.dongsan.domain.domains.crew.domain.PrivateCrew;

public class PrivateCrewTestBuilder {
    private String name = "test crew name";
    private String description = "test crew description";
    private String rule = "test crew rule";
    private String crewImageUrl = "test image";
    private Capacity capacity = new Capacity(true, 10);
    private String hashedPassword = "hashed-password";

    public PrivateCrewTestBuilder name(String name) {
        this.name = name;
        return this;
    }

    public PrivateCrewTestBuilder description(String description) {
        this.description = description;
        return this;
    }

    public PrivateCrewTestBuilder rule(String rule) {
        this.rule = rule;
        return this;
    }

    public PrivateCrewTestBuilder crewImageUrl(String crewImageUrl) {
        this.crewImageUrl = crewImageUrl;
        return this;
    }

    public PrivateCrewTestBuilder capacity(Capacity capacity) {
        this.capacity = capacity;
        return this;
    }

    public PrivateCrewTestBuilder hashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
        return this;
    }

    public PrivateCrew build() {
        return new PrivateCrew(name, description, rule, crewImageUrl, capacity, hashedPassword);
    }
}
