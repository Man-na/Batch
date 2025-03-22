package com.manna.batchservice.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.domain.EntityScan;
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
        basePackages = "com.manna.batchservice.tingthing.entity",
        entityManagerFactoryRef = "tingThingEntityManagerFactory",
        transactionManagerRef = "tingThingTransactionManager"
)
@EntityScan(
        basePackages = {"com.manna.batchservice.tingthing.entity"}
)
public class TingThingDataSourceConfig {

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean tingThingEntityManagerFactory(
            @Qualifier("tingThingDataSource") DataSource tingThingDataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(tingThingDataSource);
        em.setPackagesToScan("com.manna.batchservice.tingthing.entity");
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("hibernate.hbm2ddl.auto", "none");
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    @Primary
    public JpaTransactionManager tingThingTransactionManager(
            @Qualifier("tingThingEntityManagerFactory") EntityManagerFactory tingThingEntityManagerFactory) {
        return new JpaTransactionManager(tingThingEntityManagerFactory);
    }
}
