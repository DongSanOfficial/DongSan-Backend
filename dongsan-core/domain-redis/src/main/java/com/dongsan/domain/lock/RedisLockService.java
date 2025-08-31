package com.dongsan.domain.lock;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionTimedOutException;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
public class RedisLockService {
    private static final Logger log = LoggerFactory.getLogger(RedisLockService.class);
    private final RedissonClient redissonClient;
    private final LockTransactionExecutor lockTransactionExecutor;

    public RedisLockService(RedissonClient redissonClient, LockTransactionExecutor lockTransactionExecutor) {
        this.redissonClient = redissonClient;
        this.lockTransactionExecutor = lockTransactionExecutor;
    }

    public <T> T callWithLock(final Long crewId, final Supplier<T> supplier) {
        String key = this.generateCrewLockKey(crewId);
        final RLock rLock = redissonClient.getLock(key);
        return this.execute_v2(supplier, key, rLock);
    }

    private <T> T execute_v2(Supplier<T> supplier, String key, RLock rLock) {
        this.validTransaction();
        try {
            log.info("{} - lock 획득 시도", key);
            if (rLock.tryLock(5, 2, TimeUnit.SECONDS)) {
                log.info("{} - lock 획득 성공", key);
                return lockTransactionExecutor.execute(supplier);
            }
            throw new InterruptedException();
        } catch (TransactionTimedOutException e) {
            log.error("{} - 트랜잭션 타임아웃 발생", key);
            throw new RuntimeException("트랜잭션 처리 시간 초과", e);
        } catch (InterruptedException e) {
            log.error("{} - lock 획득 실패", key);
            throw new RuntimeException("락 획득 실패", e);
        } finally {
            this.unlock(rLock, key);
        }
    }

    private void unlock(final RLock rLock, String key) {
        try {
            rLock.unlock();
            log.info("{} - lock 해제 성공", key);
        } catch (IllegalMonitorStateException e) {
            log.warn("{} - 이미 해제된 lock 입니다.", key);
        }
    }

    private void validTransaction() {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new RuntimeException("상위 트랜잭션이 존재하는 경우 DistributedLock 을 사용할 수 없습니다.");
        }
    }


    private String generateCrewLockKey(Long crewId) {
        return "REDISSON_LOCK:crewId:" + crewId;
    }


}
