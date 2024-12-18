package com.example.targetproject.logger;

import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GlobalJpaQueryContext {

    // 메서드 호출 시간 기록
    private static final Map<String, List<Long>> methodCallTimestamps = new ConcurrentHashMap<>();
    private static final long CLEANUP_INTERVAL = 10 * 60 * 1000; // 정리 주기: 10분 (밀리초)

    static {
        // 정리 스케줄러 시작
        startCleanupScheduler();
    }

    private GlobalJpaQueryContext() {
        // 유틸리티 클래스: 인스턴스화 방지
    }

    // 메서드 호출 시간 추가
    public static void addMethodCall(String methodKey) {
        methodCallTimestamps
            .computeIfAbsent(methodKey, key -> new CopyOnWriteArrayList<>()) // 리스트 초기화
            .add(System.currentTimeMillis());
    }

    // 특정 기간 동안의 호출 횟수 반환
    public static int getCallCountInLastHour(String methodKey, long currentTime, long timeWindowMillis) {
        List<Long> timestamps = methodCallTimestamps.get(methodKey);
        if (timestamps == null) {
            return 0;
        }
        return (int) timestamps.stream()
            .filter(timestamp -> currentTime - timestamp <= timeWindowMillis)
            .count();
    }

    // 모든 호출 데이터 초기화
    public static void reset() {
        methodCallTimestamps.clear();
    }

    // 디버깅용: 모든 메서드 호출 상태 출력
    public static Map<String, Integer> getAllCallCounts(long currentTime, long timeWindowMillis) {
        Map<String, Integer> callCounts = new HashMap<>();
        methodCallTimestamps.forEach((key, timestamps) -> {
            int count = (int) timestamps.stream()
                .filter(timestamp -> currentTime - timestamp <= timeWindowMillis)
                .count();
            callCounts.put(key, count);
        });
        return callCounts;
    }

    // 정리 스케줄러 시작
    private static void startCleanupScheduler() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            long currentTime = System.currentTimeMillis();
            long timeWindowMillis = 3600 * 1000; // 1시간

            methodCallTimestamps.forEach((methodKey, timestamps) -> {
                timestamps.removeIf(timestamp -> currentTime - timestamp > timeWindowMillis);
            });

            System.out.println("Old data cleaned up at: " + new Date());
        }, CLEANUP_INTERVAL, CLEANUP_INTERVAL, TimeUnit.MILLISECONDS);
    }

}
