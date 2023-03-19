package com.panilya.testtask.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.net.URI
import java.time.Instant

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(e: MethodArgumentNotValidException): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST,
            """Passed value "${e.fieldError!!.rejectedValue as String}" is not valid"""
        )
        problemDetail.title = e.fieldError!!.defaultMessage!!
        problemDetail.type = URI.create("https://api.callhandler.com/errors/invalid-request")
        problemDetail.setProperty("errorCategory", "Generic")
        problemDetail.setProperty("timestamp", Instant.now())
        return problemDetail
    }

    @ExceptionHandler(DuplicateCustomerException::class)
    fun handleDuplicateCustomerException(e: DuplicateCustomerException): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message!!)
        problemDetail.title = "Given customer already exists"
        problemDetail.type = URI.create("https://api.callhandler.com/errors/duplicate-customer")
        problemDetail.setProperty("errorCategory", "Generic")
        problemDetail.setProperty("timestamp", Instant.now())
        return problemDetail
    }

    @ExceptionHandler(NoCustomerSearchFilterSpecifiedException::class)
    fun handleNoCustomerSearchFilterSpecifiedException(e: NoCustomerSearchFilterSpecifiedException): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.message!!)
        problemDetail.title = "No customer search filter specified"
        problemDetail.type = URI.create("https://api.callhandler.com/errors/no-customer-search-filter-specified")
        problemDetail.setProperty("errorCategory", "Generic")
        problemDetail.setProperty("timestamp", Instant.now())
        return problemDetail
    }

    @ExceptionHandler(CustomerNotFoundException::class)
    fun handleCustomerNotFoundException(e: CustomerNotFoundException): ProblemDetail {
        val problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.message!!)
        problemDetail.title = "Customer not found"
        problemDetail.type = URI.create("https://api.callhandler.com/errors/not-found")
        problemDetail.setProperty("errorCategory", "Generic")
        problemDetail.setProperty("timestamp", Instant.now())
        return problemDetail
    }

}
