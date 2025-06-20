package com.dongsan.domain.domains.crew;

import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CrewWalkCacheRepository {
    static final String FIELD_NICKNAME = "nickname";
    static final String FIELD_DISTANCE_METER = "distanceMeter";
    static final String FIELD_TIME_MIN = "timeMin";

    private final StringRedisTemplate redisTemplate;

    public CrewWalkCacheRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static String getCrewMemberWalkKey(Long crewId, Long memberId) {
        return "walk:crew:" + crewId + ":member:" + memberId;
    }

    private static String getCrewWalkKeyPattern(Long crewId) {
        return "walk:crew:" + crewId + ":member:";
    }

    public void saveCrewMemberWalk(Long crewId, Long memberId,
                                   String name, long distanceMeter, long timeMin) {
        String key = getCrewMemberWalkKey(crewId, memberId);
        Map<String, String> map = new HashMap<>();
        map.put(FIELD_NICKNAME, name);
        map.put(FIELD_DISTANCE_METER, String.valueOf(distanceMeter));
        map.put(FIELD_TIME_MIN, String.valueOf(timeMin));
        redisTemplate.opsForHash().putAll(key, map);  // HSET (hash putAll)
        redisTemplate.expire(key, Duration.ofMinutes(5));
    }

    public void deleteCrewMemberWalk(Long crewId, Long memberId) {
        String key = getCrewMemberWalkKey(crewId, memberId);
        redisTemplate.delete(key);  // DEL (delete key)
    }

    // SCAN (KEYS는 오래 걸림)
    public List<WalkData> getAllCrewMemberWalk(Long crewId) {
        List<WalkData> walks = new ArrayList<>();
        String pattern = getCrewWalkKeyPattern(crewId);

        ScanOptions options = ScanOptions.scanOptions().match(pattern).count(100).build();
        Cursor<byte[]> cursor = redisTemplate.getConnectionFactory().getConnection().scan(options);

        while (cursor.hasNext()) {
            String key = new String(cursor.next());
            Map<Object, Object> walkInfo = redisTemplate.opsForHash().entries(key);
            walks.add(WalkData.of(key, walkInfo));
        }
        return walks;
    }
}
