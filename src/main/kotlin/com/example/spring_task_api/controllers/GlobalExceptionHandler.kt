package com.example.spring_task_api.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

data class FieldErrorDetail(
    val field: String,
    val message: String
)

data class ValidationErrorPayload(
    val errors: List<FieldErrorDetail>
)

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(
        ex: MethodArgumentNotValidException
    ): ResponseEntity<ValidationErrorPayload> {
        val errorList = ex.bindingResult.fieldErrors.map { error ->
            FieldErrorDetail(
                field = error.field,
                message = error.defaultMessage ?: "Validation error"
            )
        }
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(ValidationErrorPayload(errors = errorList))
    }
}