package com.eny.paymentcollection.controller;

import com.eny.paymentcollection.dto.request.PaymentRequestDto;
import com.eny.paymentcollection.dto.response.GenericResponse;
import com.eny.paymentcollection.dto.response.PaymentResponseDto;
import com.eny.paymentcollection.enums.Currency;
import com.eny.paymentcollection.enums.PaymentStatus;
import com.eny.paymentcollection.enums.PaymentType;
import com.eny.paymentcollection.service.GenericResponseService;
import com.eny.paymentcollection.service.IPaymentService;
import examples.payment.PaymentOperationExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Payment Management", description = "Payment collection operations")
public class PaymentController {

    private final IPaymentService paymentService;
    private final GenericResponseService genericResponseService;

    @GetMapping
    @Operation(summary = "Get all payments with pagination")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ACCOUNTANT', 'ROLE_COLLECTOR', 'ROLE_MANAGER', 'ROLE_VIEWER')")
    public ResponseEntity<GenericResponse<Page<PaymentResponseDto>>> getAllPayments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "paymentDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Page<PaymentResponseDto> result = paymentService.getAllPayments(page, size, sortBy, sortDir);
        return ResponseEntity.ok(genericResponseService.createSuccessResponse("Payments retrieved successfully", result));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ACCOUNTANT', 'ROLE_COLLECTOR', 'ROLE_MANAGER', 'ROLE_VIEWER')")
    public ResponseEntity<GenericResponse<PaymentResponseDto>> getPaymentById(@PathVariable Long id) {
        PaymentResponseDto payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(genericResponseService.createSuccessResponse("Payment retrieved successfully", payment));
    }

    @PostMapping
    @Operation(
            summary = "Create new payment",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = PaymentRequestDto.class),
                            examples = @ExampleObject(name = "Sample Payment", value = PaymentOperationExamples.CREATE_PAYMENT_REQUEST_EXAMPLE)
                    )
            )
    )
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ACCOUNTANT', 'ROLE_COLLECTOR')")
    public ResponseEntity<GenericResponse<PaymentResponseDto>> createPayment(
            @Valid @RequestBody PaymentRequestDto requestDto) {
        PaymentResponseDto result = paymentService.createPayment(requestDto);
        return new ResponseEntity<>(genericResponseService.createSuccessResponse("Payment created successfully", result), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update payment")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ACCOUNTANT', 'ROLE_COLLECTOR')")
    public ResponseEntity<GenericResponse<PaymentResponseDto>> updatePayment(
            @PathVariable Long id,
            @Valid @RequestBody PaymentRequestDto requestDto) {
        PaymentResponseDto result = paymentService.updatePayment(id, requestDto);
        return ResponseEntity.ok(genericResponseService.createSuccessResponse("Payment updated successfully", result));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update payment status")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ACCOUNTANT', 'ROLE_COLLECTOR')")
    public ResponseEntity<GenericResponse<PaymentResponseDto>> updatePaymentStatus(
            @PathVariable Long id,
            @RequestParam PaymentStatus status) {
        PaymentResponseDto result = paymentService.updatePaymentStatus(id, status);
        return ResponseEntity.ok(genericResponseService.createSuccessResponse("Payment status updated successfully", result));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete payment")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<GenericResponse<Void>> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.ok(genericResponseService.createSuccessResponse("Payment deleted successfully", null));
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get payments by customer ID")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ACCOUNTANT', 'ROLE_COLLECTOR', 'ROLE_MANAGER', 'ROLE_VIEWER')")
    public ResponseEntity<GenericResponse<Page<PaymentResponseDto>>> getPaymentsByCustomer(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "paymentDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        Page<PaymentResponseDto> result = paymentService.getPaymentsByCustomer(customerId, page, size, sortBy, sortDir);
        return ResponseEntity.ok(genericResponseService.createSuccessResponse("Customer payments retrieved successfully", result));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get payments by status")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ACCOUNTANT', 'ROLE_COLLECTOR', 'ROLE_MANAGER', 'ROLE_VIEWER')")
    public ResponseEntity<GenericResponse<List<PaymentResponseDto>>> getPaymentsByStatus(@PathVariable PaymentStatus status) {
        List<PaymentResponseDto> result = paymentService.getPaymentsByStatus(status);
        return ResponseEntity.ok(genericResponseService.createSuccessResponse("Payments retrieved successfully", result));
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Get payments by type")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ACCOUNTANT', 'ROLE_COLLECTOR', 'ROLE_MANAGER', 'ROLE_VIEWER')")
    public ResponseEntity<GenericResponse<List<PaymentResponseDto>>> getPaymentsByType(@PathVariable PaymentType type) {
        List<PaymentResponseDto> result = paymentService.getPaymentsByType(type);
        return ResponseEntity.ok(genericResponseService.createSuccessResponse("Payments retrieved successfully", result));
    }

    @GetMapping("/currency/{currency}")
    @Operation(summary = "Get payments by currency")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ACCOUNTANT', 'ROLE_COLLECTOR', 'ROLE_MANAGER', 'ROLE_VIEWER')")
    public ResponseEntity<GenericResponse<List<PaymentResponseDto>>> getPaymentsByCurrency(@PathVariable Currency currency) {
        List<PaymentResponseDto> result = paymentService.getPaymentsByCurrency(currency);
        return ResponseEntity.ok(genericResponseService.createSuccessResponse("Payments retrieved successfully", result));
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get payments between two dates")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ACCOUNTANT', 'ROLE_COLLECTOR', 'ROLE_MANAGER', 'ROLE_VIEWER')")
    public ResponseEntity<GenericResponse<List<PaymentResponseDto>>> getPaymentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<PaymentResponseDto> result = paymentService.getPaymentsByDateRange(startDate, endDate);
        return ResponseEntity.ok(genericResponseService.createSuccessResponse("Payments retrieved successfully", result));
    }

    @GetMapping("/overdue")
    @Operation(summary = "Get overdue payments")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_ACCOUNTANT', 'ROLE_COLLECTOR', 'ROLE_MANAGER')")
    public ResponseEntity<GenericResponse<List<PaymentResponseDto>>> getOverduePayments() {
        List<PaymentResponseDto> result = paymentService.getOverduePayments();
        return ResponseEntity.ok(genericResponseService.createSuccessResponse("Overdue payments retrieved successfully", result));
    }
}
