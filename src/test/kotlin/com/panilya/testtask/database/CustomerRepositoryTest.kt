package com.panilya.testtask.database

import com.panilya.testtask.AbstractIT
import com.panilya.testtask.api.IncomingCallRequest
import com.panilya.testtask.fakedata.CustomerObjectMother
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class CustomerRepositoryTest : AbstractIT() {

    @Autowired
    lateinit var customerRepository: CustomerRepository

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

        val foundCustomer = customerRepository.findCustomerByPhoneNumber(customer.phoneNumber!!)
        assertThat(foundCustomer).isNotNull
        assertThat(foundCustomer!!.firstName).isEqualTo(customer.firstName)
        assertThat(foundCustomer.lastName).isEqualTo(customer.lastName)
        assertThat(foundCustomer.email).isEqualTo(customer.email)
        assertThat(foundCustomer.phoneNumber).isEqualTo(customer.phoneNumber)
        assertThat(foundCustomer.appName).isEqualTo(customer.appName)
        assertThat(foundCustomer.createdAt).isNotNull
    }

    @Test
    fun `check finding customer by email`() {
        val customer = customerRepository.save(CustomerObjectMother.createCustomer())

        val foundCustomer = customerRepository.findCustomerByEmail(customer.email!!)
        assertThat(foundCustomer).isNotNull
        assertThat(foundCustomer!!.firstName).isEqualTo(customer.firstName)
        assertThat(foundCustomer.lastName).isEqualTo(customer.lastName)
        assertThat(foundCustomer.email).isEqualTo(customer.email)
        assertThat(foundCustomer.phoneNumber).isEqualTo(customer.phoneNumber)
        assertThat(foundCustomer.appName).isEqualTo(customer.appName)
        assertThat(foundCustomer.createdAt).isNotNull
    }

}