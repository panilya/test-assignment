package com.panilya.testtask.service

import com.panilya.testtask.AbstractIT
import com.panilya.testtask.api.IncomingCallRequest
import com.panilya.testtask.database.Customer
import com.panilya.testtask.database.CustomerRepository
import com.panilya.testtask.exceptions.DuplicateCustomerException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class IncomingCallServiceTest : AbstractIT() {

    @Autowired
    lateinit var incomingCallService: IncomingCallService

    @Autowired
    lateinit var customerRepository: CustomerRepository

    @Test
    fun `test incoming call handling`() {
        val incomingCallRequest = IncomingCallRequest("Ilya", "Pantsyr", "+380501234567", "ilya@gmail.com", "Weather")

        assertThat(customerRepository.findAll()).hasSize(0)

        incomingCallService.handleIncomingCall(incomingCallRequest)

        assertThat(customerRepository.findAll()).hasSize(1)
        assertThat(customerRepository.findAll().first().firstName).isEqualTo("Ilya")
        assertThat(customerRepository.findAll().first().lastName).isEqualTo("Pantsyr")
        assertThat(customerRepository.findAll().first().phoneNumber).isEqualTo("+380501234567")
        assertThat(customerRepository.findAll().first().email).isEqualTo("ilya@gmail.com")
        assertThat(customerRepository.findAll().first().appName).isEqualTo("Weather")
    }

    @Test
    fun `test incoming call handling resulting in duplication exception`() {
        customerRepository.save(Customer().apply {
            firstName = "Ilya"
            lastName = "Pantsyr"
            phoneNumber = "+380501234567"
            email = "ilya@gmail.com"
            appName = "Weather"
        })

        val incomingCallRequest = IncomingCallRequest("Ilya", "Pantsyr", "+380501234567", "ilya@gmail.com", "Weather")

        assertThatThrownBy { incomingCallService.handleIncomingCall(incomingCallRequest) }
            .isInstanceOf(DuplicateCustomerException::class.java)
    }

}