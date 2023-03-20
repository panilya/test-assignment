package com.panilya.testtask.api

import com.panilya.testtask.service.IncomingCallService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

@RestController
@RequestMapping("/api")
@Tag(name = "Incoming call")
class IncomingCallController(
    private val incomingCallService: IncomingCallService,
    private val messages: MessageSource
) {

    @PostMapping("/incomingCall")
    @Operation(summary = "Handle incoming call", description = "Handle incoming call from the app")
    @ApiResponses(
        ApiResponse(responseCode = "201", description = "Customer created"),
        ApiResponse(responseCode = "400", description = "Invalid request")
    )
    fun handleIncomingCall(
        @Valid @RequestBody incomingCallRequest: IncomingCallRequest,
        request: HttpServletRequest
    ): ResponseEntity<GenericResponse> {
        incomingCallService.handleIncomingCall(incomingCallRequest)

        return ResponseEntity(
            GenericResponse(messages.getMessage("customer.success.created", null, request.locale)),
            HttpStatus.CREATED
        )
    }

    @GetMapping("/customer")
    @Operation(summary = "Get customers", description = "Get customers by phone number or email")
    @ApiResponses(
        ApiResponse(responseCode = "200", description = "Customer(s) found"),
        ApiResponse(responseCode = "404", description = "Customer(s) not found")
    )
    fun getCustomer(
        @RequestParam("phoneNumber", required = false) phoneNumber: String?,
        @RequestParam("email", required = false) @Email email: String?
    ): ResponseEntity<List<GetCustomerResponse>> {
        return ResponseEntity.ok(incomingCallService.getCustomers(phoneNumber, email))
    }

}

data class IncomingCallRequest(
    @field:NotBlank(message = "First name is required")
    val firstName: String,
    @field:NotBlank(message = "Last name is required")
    val lastName: String,
    @field:Pattern(
        regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}\$",
        message = "Phone number is not valid"
    )
    val phoneNumber: String,
    @field:Email(message = "Email is not valid")
    val email: String,
    @field:NotBlank(message = "App name is required")
    val appName: String,
)

data class GetCustomerResponse(
    val objectId: String?,
    val firstName: String?,
    val lastName: String?,
    val phoneNumber: String?,
    val email: String?,
    val appName: String?,
    val createdAt: Instant?
)