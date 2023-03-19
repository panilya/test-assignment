package com.panilya.testtask.exceptions

import com.panilya.testtask.api.IncomingCallRequest

class DuplicateCustomerException(incomingCallRequest: IncomingCallRequest) : RuntimeException("Customer with first name: ${incomingCallRequest.firstName}, last name: ${incomingCallRequest.lastName}, phone number: ${incomingCallRequest.phoneNumber}, email: ${incomingCallRequest.email}, app name: ${incomingCallRequest.appName} already exists")