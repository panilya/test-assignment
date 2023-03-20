package com.panilya.testtask.database

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.Instant
import java.util.UUID

@Entity
class Customer {

    var firstName: String? = null

    var lastName: String? = null

    var phoneNumber: String? = null

    var email: String? = null

    var appName: String? = null

    var createdAt: Instant? = Instant.now()

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    var objectId: String = UUID.randomUUID().toString()

}