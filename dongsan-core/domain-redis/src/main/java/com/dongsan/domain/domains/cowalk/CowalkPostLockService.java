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

    public RLock tryFairLock(Long cowalkPostId) throws InterruptedException {
        long waitTime = 10;
        long leaseTime = 2;

        RLock lock = getFairLock(cowalkPostId);

        if (lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)) {
            return lock;
        } else {
            throw new InterruptedException("CowalkPostLockService : 락 획득 실패: " + cowalkPostId);
        }
    }

    public void unlock(RLock lock) {
        lock.unlock();
    }

    public <T> T executeWithLock(Long cowalkPostId, Supplier<T> task) throws InterruptedException {
        RLock lock = tryFairLock(cowalkPostId);

        try {
            return task.get();
        } finally {
            unlock(lock);
        }
    }
}
