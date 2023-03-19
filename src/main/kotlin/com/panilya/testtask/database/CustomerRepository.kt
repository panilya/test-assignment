package com.panilya.testtask.database

import com.panilya.testtask.api.IncomingCallRequest
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CustomerRepository : JpaRepository<Customer, Long> {

    fun findByObjectId(objectId: String): Customer?

    fun findCustomerByPhoneNumber(phoneNumber: String): Customer?

    fun findCustomerByEmail(email: String): Customer?

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Customer c " +
            "WHERE c.firstName = :#{#incomingCallRequest.firstName} " +
            "AND c.lastName = :#{#incomingCallRequest.lastName} " +
            "AND c.phoneNumber = :#{#incomingCallRequest.phoneNumber} " +
            "AND c.email = :#{#incomingCallRequest.email} " +
            "AND c.appName = :#{#incomingCallRequest.appName}")
    fun checkForDuplicate(@Param("incomingCallRequest") incomingCallRequest: IncomingCallRequest): Boolean

}