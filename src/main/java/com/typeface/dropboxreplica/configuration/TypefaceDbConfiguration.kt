package com.typeface.dropboxreplica.configuration

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager


@Configuration
@EnableJpaRepositories(
    basePackages = [
        "com.typeface.dropboxreplica.repository"
    ],
    entityManagerFactoryRef = "typefaceEntityManager",
    transactionManagerRef = "typefaceTransactionManager"
)
open class TypefaceDbConfiguration(private val environment: Environment) {

    @Bean
    @ConfigurationProperties(prefix = "spring.typeface-datasource")
    open fun typefaceDataSourceProperties(): DataSourceProperties {
        return DataSourceProperties()
    }

    @Bean(destroyMethod = "close")
    open fun typefaceDataSource(typefaceDataSourceProperties: DataSourceProperties): HikariDataSource {
        val config = HikariConfig().apply {
            jdbcUrl =typefaceDataSourceProperties.url
            username = typefaceDataSourceProperties.username
            password = typefaceDataSourceProperties.password
        }
        return HikariDataSource(config)
    }

    @Bean
    open fun typefaceEntityManager(): LocalContainerEntityManagerFactoryBean {
        val entityManager = LocalContainerEntityManagerFactoryBean()
        entityManager.dataSource = typefaceDataSource(typefaceDataSourceProperties())
        entityManager.setPackagesToScan(
            *arrayOf(
                "com.typeface.DropboxReplica.entity"
            )
        )
        val vendorAdapter = HibernateJpaVendorAdapter()
        entityManager.jpaVendorAdapter = vendorAdapter
        val properties = mutableMapOf<String, Any?>()
        properties["hibernate.show_sql"] = environment.getProperty("spring.jpa.show_sql")
        properties["hibernate.hbm2ddl.auto"] = environment.getProperty("spring.jpa.hibernate.ddl-auto")
        properties["hibernate.dialect"] = environment.getProperty("spring.jpa.properties.hibernate.dialect")
        properties["hibernate.jdbc.lob.non_contextual_creation"] = environment.getProperty("spring.jpa.properties.hibernate.jdbc.lob.non_contextual_creation")
        properties["hibernate.implicit_naming_strategy"] = SpringImplicitNamingStrategy::class.java.name
        properties["hibernate.physical_naming_strategy"] = CamelCaseToUnderscoresNamingStrategy::class.java.name

        entityManager.setJpaPropertyMap(properties)
        return entityManager
    }

    @Bean
    open fun typefaceTransactionManager(): PlatformTransactionManager {
        val transactionManager = JpaTransactionManager()
        transactionManager.entityManagerFactory = typefaceEntityManager().getObject()
        return transactionManager
    }
}