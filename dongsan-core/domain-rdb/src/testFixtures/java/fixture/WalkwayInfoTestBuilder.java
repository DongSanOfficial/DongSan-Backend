package fixture;

import com.dongsan.domain.domains.walkway.domain.WalkwayExposeLevel;
import com.dongsan.domain.domains.walkway.domain.WalkwayInfo;

import java.util.List;

public class WalkwayInfoTestBuilder {
    private String name = "test name";
    private String memo = "test memo";
    private WalkwayExposeLevel exposeLevel = WalkwayExposeLevel.PUBLIC;
    private List<String> hashtags = List.of("#산책", "#공원");
    private Double distanceKm = 1.0;
    private Integer timeSec = 600;

    public WalkwayInfoTestBuilder name(String name) {
        this.name = name;
        return this;
    }

    public WalkwayInfoTestBuilder memo(String memo) {
        this.memo = memo;
        return this;
    }

    public WalkwayInfoTestBuilder exposeLevel(WalkwayExposeLevel exposeLevel) {
        this.exposeLevel = exposeLevel;
        return this;
    }

    public WalkwayInfoTestBuilder hashtags(List<String> hashtags) {
        this.hashtags = hashtags;
        return this;
    }

    public WalkwayInfoTestBuilder distanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
        return this;
    }

    public WalkwayInfoTestBuilder timeSec(Integer timeSec) {
        this.timeSec = timeSec;
        return this;
    }

    public WalkwayInfo build() {
        return new WalkwayInfo(name, distanceKm, timeSec, exposeLevel, memo, hashtags);
    }
}
