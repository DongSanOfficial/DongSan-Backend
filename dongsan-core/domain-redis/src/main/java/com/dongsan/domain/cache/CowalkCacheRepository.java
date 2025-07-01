package com.dongsan.domain.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
public class CowalkCacheRepository {

    private static final Logger log = LoggerFactory.getLogger(CowalkCacheRepository.class);
    private final StringRedisTemplate redisTemplate;

    public CowalkCacheRepository(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private static String getCowalkKey(Long cowalkId, Long memberId) {
        return "walk:cowalk:" + cowalkId + ":member:" + memberId;
    }

    private static String getCowalkKeyPattern(Long cowalkId) {
        return "walk:cowalk:" + cowalkId + ":member:*";
    }

    public int countCowalker(Long cowalkId) {
        String pattern = getCowalkKeyPattern(cowalkId);
        ScanOptions options = ScanOptions.scanOptions().match(pattern).count(100).build();

        try (Cursor<String> cursor = redisTemplate.scan(options)) {
            int count = 0;
            while (cursor.hasNext()) {
                cursor.next();
                count++;
            }
            return count;
        } catch (Exception e) {
            log.error("cowalk의 현재 산책 중인 인원 가져오는 중에 문제 발생 : {}", e.getMessage(), e);
            return 0;
        }
    }

    public void saveCowalker(Long cowalkId, Long memberId) {
        String key = getCowalkKey(cowalkId, memberId);
        redisTemplate.opsForValue().set(key, "1", Duration.ofMinutes(5));
    }

    public void deleteCowalker(Long cowalkId, Long memberId) {
        String key = getCowalkKey(cowalkId, memberId);
        redisTemplate.delete(key);
    }
}
