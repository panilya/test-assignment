package com.panilya.testtask.service

import com.panilya.testtask.api.GetCustomerResponse
import com.panilya.testtask.api.IncomingCallRequest
import com.panilya.testtask.database.Customer
import com.panilya.testtask.database.CustomerRepository
import com.panilya.testtask.exceptions.CustomerNotFoundException
import com.panilya.testtask.exceptions.DuplicateCustomerException
import com.panilya.testtask.exceptions.NoCustomerSearchFilterSpecifiedException
import org.springframework.stereotype.Service

@Service
class IncomingCallService(
    private val customerRepository: CustomerRepository
) {

    fun handleIncomingCall(incomingCallRequest: IncomingCallRequest) {
        if (customerRepository.checkForDuplicate(incomingCallRequest)) throw DuplicateCustomerException(incomingCallRequest)

        val customer = Customer()
        customer.firstName = incomingCallRequest.firstName
        customer.lastName = incomingCallRequest.lastName
        customer.phoneNumber = incomingCallRequest.phoneNumber
        customer.email = incomingCallRequest.email
        customer.appName = incomingCallRequest.appName

        customerRepository.save(customer)
    }

    fun getCustomers(phoneNumber: String?, email: String?): List<GetCustomerResponse> {
        if (phoneNumber != null && email != null) {
            val customers = customerRepository.findCustomersByPhoneNumberAndEmail(phoneNumber, email)
            if (customers != null) {
                return customers.map { it.toResponse() }
            } else {
                throw CustomerNotFoundException()
            }
        } else if (phoneNumber != null) {
            val customers = customerRepository.findCustomersByPhoneNumber(phoneNumber)
            if (customers != null) {
                return customers.map { it.toResponse() }
            } else {
                throw CustomerNotFoundException()
            }
        } else if (email != null) {
            val customers = customerRepository.findCustomersByEmail(email)
            if (customers != null) {
                return customers.map { it.toResponse() }
            } else {
                throw CustomerNotFoundException()
            }
        } else {
            throw NoCustomerSearchFilterSpecifiedException()
        }
    }

}

fun Customer.toResponse(): GetCustomerResponse {
    return GetCustomerResponse(
        objectId = this.objectId,
        firstName = this.firstName,
        lastName = this.lastName,
        phoneNumber = this.phoneNumber,
        email = this.email,
        appName = this.appName,
        createdAt = this.createdAt
    )
}