package com.eny.paymentcollection.controller;

import com.eny.paymentcollection.dto.request.PaymentDto;
import com.eny.paymentcollection.dto.response.GenericResponse;
import com.eny.paymentcollection.enums.Currency;
import com.eny.paymentcollection.enums.PaymentStatus;
import com.eny.paymentcollection.enums.PaymentType;
import com.eny.paymentcollection.service.GenericResponseService;
import com.eny.paymentcollection.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Payment Management", description = "Payment collection operations")
public class PaymentController {

    private final PaymentService paymentService;
    private final GenericResponseService genericResponseService;

    @GetMapping
    @Operation(summary = "Get all payments with pagination")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ACCOUNTANT') or hasRole('COLLECTOR') or hasRole('MANAGER') or hasRole('VIEWER')")
    public ResponseEntity<GenericResponse<Page<PaymentDto>>> getAllPayments(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "paymentDate") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

        log.debug("Getting all payments - page: {}, size: {}, sortBy: {}, sortDir: {}",
                page, size, sortBy, sortDir);

        Page<PaymentDto> payments = paymentService.getAllPayments(page, size, sortBy, sortDir);
        GenericResponse<Page<PaymentDto>> response = genericResponseService
                .createResponseNoError("Payments retrieved successfully", payments);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get payment by ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ACCOUNTANT') or hasRole('COLLECTOR') or hasRole('MANAGER') or hasRole('VIEWER')")
    public ResponseEntity<GenericResponse<PaymentDto>> getPaymentById(
            @Parameter(description = "Payment ID") @PathVariable Long id) {

        log.debug("Getting payment by id: {}", id);

        PaymentDto payment = paymentService.getPaymentById(id);
        GenericResponse<PaymentDto> response = genericResponseService
                .createResponseNoError("Payment retrieved successfully", payment);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get payments by customer")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ACCOUNTANT') or hasRole('COLLECTOR') or hasRole('MANAGER') or hasRole('VIEWER')")
    public ResponseEntity<GenericResponse<Page<PaymentDto>>> getPaymentsByCustomer(
            @Parameter(description = "Customer ID") @PathVariable Long customerId,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "paymentDate") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "desc") String sortDir) {

        log.debug("Getting payments for customer id: {}", customerId);

        Page<PaymentDto> payments = paymentService.getPaymentsByCustomer(customerId, page, size, sortBy, sortDir);
        GenericResponse<Page<PaymentDto>> response = genericResponseService
                .createResponseNoError("Customer payments retrieved successfully", payments);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create new payment")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ACCOUNTANT') or hasRole('COLLECTOR')")
    public ResponseEntity<GenericResponse<PaymentDto>> createPayment(
            @Parameter(description = "Payment data") @Valid @RequestBody PaymentDto paymentDto) {

        log.debug("Creating new payment for customer id: {}", paymentDto.getCustomerId());

        PaymentDto createdPayment = paymentService.createPayment(paymentDto);
        GenericResponse<PaymentDto> response = genericResponseService
                .createResponseNoError("Payment created successfully", createdPayment);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update payment")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ACCOUNTANT') or hasRole('COLLECTOR')")
    public ResponseEntity<GenericResponse<PaymentDto>> updatePayment(
            @Parameter(description = "Payment ID") @PathVariable Long id,
            @Parameter(description = "Updated payment data") @Valid @RequestBody PaymentDto paymentDto) {

        log.debug("Updating payment with id: {}", id);

        PaymentDto updatedPayment = paymentService.updatePayment(id, paymentDto);
        GenericResponse<PaymentDto> response = genericResponseService
                .createResponseNoError("Payment updated successfully", updatedPayment);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update payment status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ACCOUNTANT') or hasRole('COLLECTOR')")
    public ResponseEntity<GenericResponse<PaymentDto>> updatePaymentStatus(
            @Parameter(description = "Payment ID") @PathVariable Long id,
            @Parameter(description = "New payment status") @RequestParam PaymentStatus status) {

        log.debug("Updating payment status with id: {} to status: {}", id, status);

        PaymentDto updatedPayment = paymentService.updatePaymentStatus(id, status);
        GenericResponse<PaymentDto> response = genericResponseService
                .createResponseNoError("Payment status updated successfully", updatedPayment);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete payment")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponse<Void>> deletePayment(
            @Parameter(description = "Payment ID") @PathVariable Long id) {

        log.debug("Deleting payment with id: {}", id);

        paymentService.deletePayment(id);
        GenericResponse<Void> response = genericResponseService
                .createResponseNoError("Payment deleted successfully", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get payments by status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ACCOUNTANT') or hasRole('COLLECTOR') or hasRole('MANAGER') or hasRole('VIEWER')")
    public ResponseEntity<GenericResponse<List<PaymentDto>>> getPaymentsByStatus(
            @Parameter(description = "Payment status") @PathVariable PaymentStatus status) {

        log.debug("Getting payments by status: {}", status);

        List<PaymentDto> payments = paymentService.getPaymentsByStatus(status);
        GenericResponse<List<PaymentDto>> response = genericResponseService
                .createResponseNoError("Payments retrieved successfully", payments);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/date-range")
    @Operation(summary = "Get payments by date range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ACCOUNTANT') or hasRole('COLLECTOR') or hasRole('MANAGER') or hasRole('VIEWER')")
    public ResponseEntity<GenericResponse<List<PaymentDto>>> getPaymentsByDateRange(
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        log.debug("Getting payments between dates: {} and {}", startDate, endDate);

        List<PaymentDto> payments = paymentService.getPaymentsByDateRange(startDate, endDate);
        GenericResponse<List<PaymentDto>> response = genericResponseService
                .createResponseNoError("Payments retrieved successfully", payments);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/overdue")
    @Operation(summary = "Get overdue payments")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ACCOUNTANT') or hasRole('COLLECTOR') or hasRole('MANAGER')")
    public ResponseEntity<GenericResponse<List<PaymentDto>>> getOverduePayments() {

        log.debug("Getting overdue payments");

        List<PaymentDto> payments = paymentService.getOverduePayments();
        GenericResponse<List<PaymentDto>> response = genericResponseService
                .createResponseNoError("Overdue payments retrieved successfully", payments);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/type/{paymentType}")
    @Operation(summary = "Get payments by type")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ACCOUNTANT') or hasRole('COLLECTOR') or hasRole('MANAGER') or hasRole('VIEWER')")
    public ResponseEntity<GenericResponse<List<PaymentDto>>> getPaymentsByType(
            @Parameter(description = "Payment type") @PathVariable PaymentType paymentType) {

        log.debug("Getting payments by type: {}", paymentType);

        List<PaymentDto> payments = paymentService.getPaymentsByType(paymentType);
        GenericResponse<List<PaymentDto>> response = genericResponseService
                .createResponseNoError("Payments retrieved successfully", payments);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/currency/{currency}")
    @Operation(summary = "Get payments by currency")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ACCOUNTANT') or hasRole('COLLECTOR') or hasRole('MANAGER') or hasRole('VIEWER')")
    public ResponseEntity<GenericResponse<List<PaymentDto>>> getPaymentsByCurrency(
            @Parameter(description = "Currency") @PathVariable Currency currency) {

        log.debug("Getting payments by currency: {}", currency);

        List<PaymentDto> payments = paymentService.getPaymentsByCurrency(currency);
        GenericResponse<List<PaymentDto>> response = genericResponseService
                .createResponseNoError("Payments retrieved successfully", payments);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}