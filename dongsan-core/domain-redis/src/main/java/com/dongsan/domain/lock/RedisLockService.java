package com.dongsan.domain.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
public class RedisLockService {
    private static final Logger log = LoggerFactory.getLogger(RedisLockService.class);
    private final RedissonClient redissonClient;
    private final ApplicationEventPublisher applicationEventPublisher;

    public RedisLockService(RedissonClient redissonClient, ApplicationEventPublisher applicationEventPublisher) {
        this.redissonClient = redissonClient;
        this.applicationEventPublisher = applicationEventPublisher;
    }
    
    public <T> T callWithLock(final Long crewId, final Supplier<T> supplier) {
        String key = this.generateCrewLockKey(crewId);
        final RLock rLock = redissonClient.getLock(key);
        return this.execute(supplier, key, rLock);
    }

    private <T> T execute(Supplier<T> supplier, String key, RLock rLock) {
        try {
            log.info("{} - lock 획득 시도", key);
            if (rLock.tryLock(5, 10, TimeUnit.SECONDS)) {
                log.info("{} - lock 획득 성공", key);
                return supplier.get();
            }
            throw new InterruptedException();
        } catch (InterruptedException e) {
            log.error("{} - lock 획득 실패", key);
            throw new RuntimeException("락 획득 실패", e);
        } finally {
            applicationEventPublisher.publishEvent(new RedisLockEvent(key, rLock));
        }
    }

    private String generateCrewLockKey(Long crewId) {
        return "REDISSON_LOCK:crewId:" + crewId;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMPLETION)
    public void subscribeUnlock(final RedisLockEvent lockEvent) {
        try {
            lockEvent.unlock();
            log.info("{} - lock 해제 성공", lockEvent.key());
        } catch (IllegalMonitorStateException e) {
            log.warn("{} - 이미 해제된 lock 입니다.", lockEvent.key());
        }
    }

    private record RedisLockEvent(String key, RLock rLock) {
        public void unlock() {
            this.rLock.unlock();
        }
    }
}
