package com.panilya.testtask.fakedata

import com.panilya.testtask.database.Customer
import net.datafaker.Faker

object CustomerObjectMother {

    private val faker = Faker()

    fun createCustomer(email: String? = faker.internet().emailAddress(), phoneNumber: String? = faker.phoneNumber().phoneNumberInternational()): Customer {
        val customer = Customer()
        customer.firstName = faker.name().firstName()
        customer.lastName = faker.name().lastName()
        customer.email = email
        customer.phoneNumber = phoneNumber
        customer.appName = faker.app().name()
        return customer
    }

}