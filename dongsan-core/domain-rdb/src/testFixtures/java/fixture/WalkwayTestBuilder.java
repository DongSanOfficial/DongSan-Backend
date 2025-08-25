package fixture;

import com.dongsan.domain.domains.walkway.domain.Walkway;
import com.dongsan.domain.domains.walkway.domain.WalkwayGeometry;
import com.dongsan.domain.domains.walkway.domain.WalkwayInfo;

public class WalkwayTestBuilder {
    private Long memberId = 1L;
    private WalkwayInfo walkwayInfo = new WalkwayInfoTestBuilder().build();
    private WalkwayGeometry geometry = new WalkwayGeometryTestBuilder().build();

    public WalkwayTestBuilder memberId(Long memberId) {
        this.memberId = memberId;
        return this;
    }

    public WalkwayTestBuilder walkwayInfo(WalkwayInfo walkwayInfo) {
        this.walkwayInfo = walkwayInfo;
        return this;
    }

    public WalkwayTestBuilder geometry(WalkwayGeometry geometry) {
        this.geometry = geometry;
        return this;
    }

    public Walkway build() {
        return new Walkway(memberId, walkwayInfo, geometry);
    }
}
