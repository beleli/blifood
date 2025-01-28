package br.com.blifood.core.config

import br.com.blifood.core.properties.DataSourceProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import javax.sql.DataSource

@Configuration
@EnableConfigurationProperties(DataSourceProperties::class)
class DataSourceConfig(
    private val environment: Environment,
    private val dataSourceProperties: DataSourceProperties
) {

    @Bean
    fun dataSource(): DataSource {
        val dataSource = org.springframework.jdbc.datasource.DriverManagerDataSource()
        dataSource.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name")!!)
        dataSource.schema = environment.getProperty("spring.jpa.properties.hibernate.default_schema")
        dataSource.url = environment.getProperty("spring.datasource.url")
        dataSource.username = dataSourceProperties.username
        dataSource.password = dataSourceProperties.password
        return dataSource
    }
}
