package com.dongsan.domain.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

@Repository
public class LockRepository {
    private final RedissonClient redissonClient;

    public LockRepository(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public RLock getFairLock(String key) {
        return redissonClient.getFairLock(key);
    }
}
