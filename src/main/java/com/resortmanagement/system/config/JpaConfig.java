/*
JpaConfig.java
Purpose:
 - Enable JPA auditing, repositories, and transaction management.
 - Optionally configure Hibernate properties (ddl-auto, show_sql, hibernate.jdbc.batch_size).
 - Optionally configure PhysicalNamingStrategy or implicit naming strategy consistent with Flyway.

File: config/JpaConfig.java
*/
package com.resortmanagement.system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "com.resortmanagement.system")
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
@EnableTransactionManagement
public class JpaConfig {
}