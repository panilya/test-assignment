package com.panilya.testtask.api

import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError

open class GenericResponse {
    val message: String
    val error: String?

    constructor(message: String, error: String? = null) : super() {
        this.message = message
        this.error = error
    }

    constructor(allErrors: List<ObjectError>, error: String) {
        this.error = error
        val temp = allErrors.joinToString(separator = ",") { e: ObjectError ->
            if (e is FieldError) {
                """{"field":"${e.field}","defaultMessage":"${e.getDefaultMessage()}"}"""
            } else {
                """{"object":"${e.objectName}","defaultMessage":"${e.defaultMessage}"}"""
            }
        }
        message = "[$temp]"
    }
}
