package com.panilya.testtask.config

import com.panilya.testtask.database.Customer
import com.panilya.testtask.database.CustomerRepository
import com.panilya.testtask.fakedata.CustomerObjectMother
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Configuration
@Profile("dev")
class DevelopmentConfig {

    @Bean
    fun init(
        customerRepository: CustomerRepository
    ) = CommandLineRunner {

        // Flag to clear database on startup
        val clear = true
        // Flag to fill database on startup with some data
        val fillDatabase = true

        if (clear) {
            println("Clearing database")
            customerRepository.deleteAll()
        }

        if (fillDatabase) {
            println("Filling database")

            val ilyaCustomer = Customer().apply {
                firstName = "Ilya"
                lastName = "Pantsyr"
                email = "ilya@gmail.com"
                phoneNumber = "+380123456789"
                appName = "Weather APP"
            }

            customerRepository.saveAll(listOf(ilyaCustomer, CustomerObjectMother.createCustomer()))
        }
    }

}