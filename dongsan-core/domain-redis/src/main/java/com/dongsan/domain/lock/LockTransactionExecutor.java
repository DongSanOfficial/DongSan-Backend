package com.dongsan.domain.lock;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Supplier;

@Component
public class LockTransactionExecutor {

    @Transactional(timeout = 1)  // TransactionTimedOutException 발생 후 롤백
    public <T> T execute(final Supplier<T> supplier) {
        return supplier.get();
    }
}
