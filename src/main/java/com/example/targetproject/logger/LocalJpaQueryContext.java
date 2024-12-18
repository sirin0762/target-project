package com.example.targetproject.logger;

import com.example.targetproject.logger.dto.LocalQueryCounter;

public class LocalJpaQueryContext {

    private static final ThreadLocal<LocalQueryCounter> threadLocal = new ThreadLocal<>();

    public static void setLocalQueryCounter(LocalQueryCounter localQueryCounter) {
        threadLocal.set(localQueryCounter);
    }

    public static LocalQueryCounter getLocalQueryCounter() {
        return threadLocal.get();
    }

}
