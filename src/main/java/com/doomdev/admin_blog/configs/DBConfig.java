package com.doomdev.admin_blog.configs;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypes;
import org.springframework.orm.jpa.persistenceunit.PersistenceManagedTypesScanner;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(basePackages = {
		"com.doomdev.admin_blog.repositories",
		"com.doomdev.admin_blog.daos"
}, entityManagerFactoryRef = "entityManager", transactionManagerRef = "productTransactionManager")
public class DBConfig {
	public static final String JPA_QUERY_FACTORY = "adminBlogFactory";

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean("entityManager")
	@Primary
	public LocalContainerEntityManagerFactoryBean entityManager() {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());
		em.setPackagesToScan("com.doomdev.admin_blog.entities", "com.doomdev.admin_blog.dtos");

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		em.setJpaVendorAdapter(vendorAdapter);
		em.setJpaPropertyMap(new HashMap<>());
		em.setPersistenceUnitName("admin-blog");

		return em;
	}

	@Bean
	public PlatformTransactionManager productTransactionManager(@Qualifier("entityManager") LocalContainerEntityManagerFactoryBean entityManager) {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManager.getObject());
		return transactionManager;
	}

	@Bean(JPA_QUERY_FACTORY)
	public JPAQueryFactory qportalJPAQueryFactory(@Qualifier("entityManager") EntityManager entityManager) {
		return new JPAQueryFactory(entityManager);
	}
}
