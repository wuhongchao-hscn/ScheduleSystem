package com.hitachi.schedule.controller.configuration;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
// 扫描 Mapper 接口并容器管理
@MapperScan(basePackages = DataSourceConfigMybatis.PACKAGE, sqlSessionFactoryRef = "mybatisSqlSessionFactory")
public class DataSourceConfigMybatis {

    // 精确到 mybatis 目录，以便跟其他数据源隔离
    static final String PACKAGE = "com.hitachi.schedule.mybatis.mapper";
    static final String MAPPER_LOCATION = "classpath:com/hitachi/schedule/mybatis/mapper/*.xml";

    @Bean(name = "mybatisDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    @Primary
    public DataSource mybatisDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "mybatisTransactionManager")
    @Primary
    public DataSourceTransactionManager mybatisTransactionManager() {
        return new DataSourceTransactionManager(mybatisDataSource());
    }

    @Bean(name = "mybatisSqlSessionFactory")
    @Primary
    public SqlSessionFactory mybatisSqlSessionFactory(@Qualifier("mybatisDataSource") DataSource mybatisDataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(mybatisDataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(DataSourceConfigMybatis.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}
