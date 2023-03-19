package com.panilya.testtask

import jakarta.transaction.Transactional
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.support.TestPropertySourceUtils
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = [AbstractIT.DockerPostgreDataSourceInitializer::class])
@Testcontainers
@Transactional
abstract class AbstractIT {

    companion object {
        var postgreDBContainer: PostgreSQLContainer<*> = KotlinPostgresSQLContainer("postgres")
    }

    init {
        postgreDBContainer.start()
    }

    class DockerPostgreDataSourceInitializer : ApplicationContextInitializer<ConfigurableApplicationContext?> {
        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "spring.datasource.url=" + postgreDBContainer.jdbcUrl,
                "spring.datasource.username=" + postgreDBContainer.username,
                "spring.datasource.password=" + postgreDBContainer.password
            )
        }
    }
}

class KotlinPostgresSQLContainer(imageName: String) : PostgreSQLContainer<KotlinPostgresSQLContainer>(imageName)
