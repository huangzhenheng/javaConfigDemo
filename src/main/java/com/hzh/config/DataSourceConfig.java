package com.hzh.config;

import com.hzh.RootConfig;
import org.apache.commons.dbcp2.BasicDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.io.IOException;

/**
 * @Description 数据库与mybatis的配置
 * @auther hzh
 * @create 2018-11-22 15:23
 */
@Configuration
public class DataSourceConfig extends RootConfig {
    @Bean
    public BasicDataSource dataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(environment.getProperty("jdbc.driver"));
        basicDataSource.setUrl(environment.getProperty("jdbc.url"));
        basicDataSource.setUsername(environment.getProperty("jdbc.username"));
        basicDataSource.setPassword(environment.getProperty("jdbc.password"));

        basicDataSource.setMaxWaitMillis(Integer.parseInt(environment.getProperty("jdbc.maxwait")));
        basicDataSource.setMaxIdle(Integer.parseInt(environment.getProperty("jdbc.maxidle")));
        basicDataSource.setMinIdle(Integer.parseInt(environment.getProperty("jdbc.minidle")));
        basicDataSource.setMaxTotal(Integer.parseInt(environment.getProperty("jdbc.maxtotal")));
        basicDataSource.setInitialSize(Integer.parseInt(environment.getProperty("jdbc.initsize")));
        return basicDataSource;
    }

    @Bean
    public DataSourceTransactionManager transactionManager() {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource());
        return dataSourceTransactionManager;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactory() throws IOException {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // 设置数据库连接池
        sqlSessionFactoryBean.setDataSource(dataSource());
        // 配置别名所在的包，别名默认为类名
        sqlSessionFactoryBean.setTypeAliasesPackage("com.hzh.pojo");

        // 配置Mapper XML所在的位置
        ResourcePatternResolver patternResolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = patternResolver.getResources("classpath:mapper/*.xml");
        sqlSessionFactoryBean.setMapperLocations(resources);
        return sqlSessionFactoryBean;
    }
}
