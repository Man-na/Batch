package com.manna.batchservice.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "batchEntityManagerFactory",
        transactionManagerRef = "batchTransactionManager"
)
public class BatchDataSourceConfig {

    @Bean
    public LocalContainerEntityManagerFactoryBean batchEntityManagerFactory(
            @Qualifier("batchDataSource") DataSource batchDataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(batchDataSource);
        em.setPackagesToScan("com.manna.batchservice.entity");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("hibernate.hbm2ddl.auto", "none");
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    public JpaTransactionManager batchTransactionManager(
            @Qualifier("batchEntityManagerFactory") EntityManagerFactory batchEntityManagerFactory) {
        return new JpaTransactionManager(batchEntityManagerFactory);
    }
}