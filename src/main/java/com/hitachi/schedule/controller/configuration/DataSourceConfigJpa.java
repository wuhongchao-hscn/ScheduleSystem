package com.hitachi.schedule.controller.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "jpaEntityManagerFactory",
        transactionManagerRef = "jpaTransactionManager",
        basePackages = {"com.hitachi.schedule.jpa.dao"})
public class DataSourceConfigJpa {

    @Bean(name = "jpaDataSource")
    @ConfigurationProperties(prefix = "jpa.datasource")
    public DataSource customerDataSource() {
        return DataSourceBuilder.create().build();
    }


    @Bean(name = "jpaEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder, @Qualifier("jpaDataSource") DataSource dataSource) {
        return builder.dataSource(dataSource).packages("com.hitachi.schedule.jpa.entity").build();
    }

    @Bean(name = "jpaTransactionManager")
    public PlatformTransactionManager customerTransactionManager(@Qualifier("jpaEntityManagerFactory") EntityManagerFactory jpaEntityManagerFactory) {
        return new JpaTransactionManager(jpaEntityManagerFactory);
    }

}
