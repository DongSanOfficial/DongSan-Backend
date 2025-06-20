package com.dongsan.domain.domains.crew;

import java.util.Map;

import static com.dongsan.domain.domains.crew.CrewWalkCacheRepository.*;

public record WalkData(
        String memberId,
        String nickname,
        String distanceMeter,
        String timeMin
) {
    public static WalkData of(String key, Map<Object, Object> walkInfo) {
        String[] parts = key.split(":");
        String memberId = parts[parts.length - 1];

        String nickname = (String) walkInfo.getOrDefault(FIELD_NICKNAME, "");
        String distanceMeter = (String) walkInfo.getOrDefault(FIELD_DISTANCE_METER, "0");
        String timeMin = (String) walkInfo.getOrDefault(FIELD_TIME_MIN, "0");

        return new WalkData(memberId, nickname, distanceMeter, timeMin);
    }
}
