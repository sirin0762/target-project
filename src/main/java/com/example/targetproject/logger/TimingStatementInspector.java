package com.example.targetproject.logger;

import org.hibernate.resource.jdbc.spi.StatementInspector;

import java.util.concurrent.ConcurrentHashMap;

public class TimingStatementInspector implements StatementInspector {

    private static final ConcurrentHashMap<String, Long> queryStartTimes = new ConcurrentHashMap<>();

    @Override
    public String inspect(String sql) {
        long startTime = System.currentTimeMillis();
        queryStartTimes.put(sql, startTime);
        return sql;
    }

    public static Long getStartTime(String sql) {
        return queryStartTimes.get(sql);
    }

    public static void clearStartTime(String sql) {
        queryStartTimes.remove(sql);
    }
}