package com.dongsan.domain.domains.cowalk;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.redisson.api.RLock;
import org.springframework.stereotype.Service;

@Service
public class CowalkPostLockService {
    private final LockRepository lockRepository;

    public CowalkPostLockService(LockRepository cowalkPostLockRepository) {
        this.lockRepository = cowalkPostLockRepository;
    }

    public RLock getFairLock(Long cowalkPostId) {
        String key = "cowalkPost:lock:" + cowalkPostId;
        return lockRepository.getFairLock(key);
    }

    public <T> T executeWithFairLock(Long cowalkPostId, Supplier<T> task) throws InterruptedException {
        long waitTime = 10;
        long leaseTime = 2;

        RLock lock = getFairLock(cowalkPostId);

        boolean isLocked = lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        if (!isLocked) {
            throw new InterruptedException("CowalkPostLockService : 락 획득 실패 " + cowalkPostId);
        }

        try {
            return task.get();
        } finally {
            lock.unlock();
        }
    }
}
