package br.com.blifood.core.config

import br.com.blifood.core.secretsmanager.getSecretValue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient
import javax.sql.DataSource

@Configuration
class BaseConfig(
    private val environment: Environment,
    private val secretsManagerClient: SecretsManagerClient
) {

    @Bean
    fun dataSource(): DataSource {
        val dataSource = org.springframework.jdbc.datasource.DriverManagerDataSource()
        dataSource.setDriverClassName(environment.getProperty("spring.datasource.driver-class-name")!!)
        dataSource.schema = environment.getProperty("spring.jpa.properties.hibernate.default_schema")
        dataSource.url = environment.getProperty("spring.datasource.url")
        dataSource.username = secretsManagerClient.getSecretValue(environment.getProperty("spring.datasource.username")!!)
        dataSource.password = secretsManagerClient.getSecretValue(environment.getProperty("spring.datasource.password")!!)
        return dataSource
    }
}
