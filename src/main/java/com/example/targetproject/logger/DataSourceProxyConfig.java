package com.example.targetproject.logger;

import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;

import javax.sql.DataSource;
import java.util.List;

//@Configuration
@Order(Integer.MAX_VALUE)
public class DataSourceProxyConfig {

    @Bean
    public DataSource dataSource(DataSource actualDataSource) {
        return ProxyDataSourceBuilder.create(actualDataSource)
            .listener(new QueryExecutionListener() {
                @Override
                public void beforeQuery(ExecutionInfo executionInfo, List<QueryInfo> queryInfoList) {
                    // SQL 실행 전 작업 (필요 시 구현)
                }

                @Override
                public void afterQuery(ExecutionInfo executionInfo, List<QueryInfo> queryInfoList) {
                    for (QueryInfo queryInfo : queryInfoList) {
                        String sql = queryInfo.getQuery();
                        Long startTime = TimingStatementInspector.getStartTime(sql);
                        if (startTime != null) {
                            long executionTime = System.currentTimeMillis() - startTime;
                            System.out.println("SQL: " + sql + " executed in " + executionTime + "ms");
                            TimingStatementInspector.clearStartTime(sql);
                        }
                    }
                }
            })
            .build();
    }
}