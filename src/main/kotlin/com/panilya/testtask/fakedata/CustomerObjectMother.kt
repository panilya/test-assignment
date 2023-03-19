package com.panilya.testtask.fakedata

import com.panilya.testtask.database.Customer
import net.datafaker.Faker

object CustomerObjectMother {

    private val faker = Faker()

    fun createCustomer(): Customer {
        val customer = Customer()
        customer.firstName = faker.name().firstName()
        customer.lastName = faker.name().lastName()
        customer.email = faker.internet().emailAddress()
        customer.phoneNumber = faker.phoneNumber().phoneNumberInternational()
        customer.appName = faker.app().name()
        return customer
    }

}