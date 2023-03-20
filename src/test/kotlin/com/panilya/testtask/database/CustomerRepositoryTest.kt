package com.panilya.testtask.database

import com.panilya.testtask.api.IncomingCallRequest
import com.panilya.testtask.fakedata.CustomerObjectMother
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles


@SpringBootTest
@ActiveProfiles("test")
class CustomerRepositoryTest {

    @Autowired
    lateinit var customerRepository: CustomerRepository

    // Work around, because TestContainers are being shared between tests
    @BeforeEach
    fun clearDatabase() {
        customerRepository.deleteAll()
    }

    @Test
    fun `save customer`() {
        val customer = Customer().apply {
            firstName = "Ilya"
            lastName = "Pantsyr"
            email = "ilya@gmail.com"
            phoneNumber = "+380123456789"
            appName = "Weather"
        }
        customerRepository.save(customer)

        assertThat(customerRepository.findAll()).hasSize(1)

        assertThat(customerRepository.findByObjectId(customer.objectId)!!.firstName).isEqualTo("Ilya")
        assertThat(customerRepository.findByObjectId(customer.objectId)!!.lastName).isEqualTo("Pantsyr")
        assertThat(customerRepository.findByObjectId(customer.objectId)!!.email).isEqualTo("ilya@gmail.com")
        assertThat(customerRepository.findByObjectId(customer.objectId)!!.phoneNumber).isEqualTo("+380123456789")
        assertThat(customerRepository.findByObjectId(customer.objectId)!!.appName).isEqualTo("Weather")
        assertThat(customerRepository.findByObjectId(customer.objectId)!!.createdAt).isNotNull
    }

    @Test
    fun `check duplicate customer`() {
        val customer = customerRepository.save(CustomerObjectMother.createCustomer())

        assertThat(customerRepository.findAll()).hasSize(1)

        val isDuplicate = customerRepository.checkForDuplicate(
            IncomingCallRequest(customer.firstName!!, customer.lastName!!, customer.phoneNumber!!, customer.email!!, customer.appName!!)
        )
        assertThat(isDuplicate).isTrue

        val notDuplicate = customerRepository.checkForDuplicate(
            IncomingCallRequest("John", "Pantsyr", "+380123456789", "john@gmail.com", "Weather")
        )
        assertThat(notDuplicate).isFalse
    }

    @Test
    fun `check finding customer by phone number`() {
        val customer = customerRepository.save(CustomerObjectMother.createCustomer())

        assertThat(customerRepository.findAll()).hasSize(1)

        val foundCustomer = customerRepository.findCustomersByPhoneNumber(customer.phoneNumber!!)
        assertThat(foundCustomer).hasSize(1)
        assertThat(foundCustomer!!.first().firstName).isEqualTo(customer.firstName)
        assertThat(foundCustomer.first().lastName).isEqualTo(customer.lastName)
        assertThat(foundCustomer.first().email).isEqualTo(customer.email)
        assertThat(foundCustomer.first().phoneNumber).isEqualTo(customer.phoneNumber)
        assertThat(foundCustomer.first().appName).isEqualTo(customer.appName)
        assertThat(foundCustomer.first().createdAt).isNotNull
    }

    @Test
    fun `check finding customer by email`() {
        val customer = customerRepository.save(CustomerObjectMother.createCustomer())

        assertThat(customerRepository.findAll()).hasSize(1)

        val foundCustomer = customerRepository.findCustomersByEmail(customer.email!!)
        assertThat(foundCustomer).isNotNull
        assertThat(foundCustomer!!.first().firstName).isEqualTo(customer.firstName)
        assertThat(foundCustomer.first().lastName).isEqualTo(customer.lastName)
        assertThat(foundCustomer.first().email).isEqualTo(customer.email)
        assertThat(foundCustomer.first().phoneNumber).isEqualTo(customer.phoneNumber)
        assertThat(foundCustomer.first().appName).isEqualTo(customer.appName)
        assertThat(foundCustomer.first().createdAt).isNotNull
    }

    @Test
    fun `check finding customer by email and phone number`() {
        val customer = customerRepository.save(CustomerObjectMother.createCustomer())

        assertThat(customerRepository.findAll()).hasSize(1)

        val foundCustomer = customerRepository.findCustomersByPhoneNumberAndEmail(customer.phoneNumber!!, customer.email!!)
        assertThat(foundCustomer).isNotNull
        assertThat(foundCustomer).hasSize(1)
        assertThat(foundCustomer!!.first().firstName).isEqualTo(customer.firstName)
        assertThat(foundCustomer.first().lastName).isEqualTo(customer.lastName)
        assertThat(foundCustomer.first().email).isEqualTo(customer.email)
        assertThat(foundCustomer.first().phoneNumber).isEqualTo(customer.phoneNumber)
        assertThat(foundCustomer.first().appName).isEqualTo(customer.appName)
        assertThat(foundCustomer.first().createdAt).isNotNull
    }

}