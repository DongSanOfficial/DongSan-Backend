package com.dongsan.domain.lock;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Component
@Transactional
public class RedisLockTransactionExecutor {
    public <T> T execute(final Supplier<T> supplier) {
        return supplier.get();
    }
}
