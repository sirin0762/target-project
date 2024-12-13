package com.example.targetproject.logger;

import com.example.targetproject.logger.dto.LocalQueryCounter;
import org.hibernate.resource.jdbc.spi.StatementInspector;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class JpaQueryInspector implements StatementInspector {

    private final ThreadLocal<LocalQueryCounter> threadLocal = new ThreadLocal<>();

    @Override
    public String inspect(String sql) {
        LocalQueryCounter localQueryCounter = threadLocal.get();

        if (Objects.nonNull(localQueryCounter)) {
            localQueryCounter.appendQuery(sql);
        }
        return sql;
    }

}
