package com.example.targetproject.logger;

import net.ttddyy.dsproxy.ExecutionInfo;
import net.ttddyy.dsproxy.QueryInfo;
import net.ttddyy.dsproxy.listener.QueryExecutionListener;
import net.ttddyy.dsproxy.support.ProxyDataSourceBuilder;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;

@Configuration
public class DataSourceProxyInitializer implements InitializingBean {

    private final DataSource actualDataSource;

    public DataSourceProxyInitializer(DataSource actualDataSource) {
        this.actualDataSource = actualDataSource;
    }

    @Override
    public void afterPropertiesSet() {
        ProxyDataSourceBuilder.create(actualDataSource)
            .listener(new QueryExecutionListener() {
                @Override
                public void beforeQuery(ExecutionInfo executionInfo, List<QueryInfo> queryInfoList) {
                    // SQL 실행 전 작업
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
        System.out.println("ProxyDataSource has been initialized");
    }
}