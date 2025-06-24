package com.eny.paymentcollection.controller;

import com.eny.paymentcollection.dto.request.CustomerDto;
import com.eny.paymentcollection.dto.response.GenericResponse;
import com.eny.paymentcollection.enums.CustomerStatus;
import com.eny.paymentcollection.service.CustomerService;
import com.eny.paymentcollection.service.GenericResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Customer Management", description = "Customer operations")
public class CustomerController {

    private final CustomerService customerService;
    private final GenericResponseService genericResponseService;

    @GetMapping
    @Operation(summary = "Get all customers with pagination")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ACCOUNTANT') or hasRole('MANAGER') or hasRole('VIEWER')")
    public ResponseEntity<GenericResponse<Page<CustomerDto>>> getAllCustomers(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "companyName") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {

        log.debug("Getting all customers - page: {}, size: {}, sortBy: {}, sortDir: {}",
                page, size, sortBy, sortDir);

        Page<CustomerDto> customers = customerService.getAllCustomers(page, size, sortBy, sortDir);
        GenericResponse<Page<CustomerDto>> response = genericResponseService.createResponseNoError("Customers retrieved successfully", customers);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ACCOUNTANT') or hasRole('MANAGER') or hasRole('VIEWER')")
    public ResponseEntity<GenericResponse<CustomerDto>> getCustomerById(
            @Parameter(description = "Customer ID") @PathVariable Long id) {

        log.debug("Getting customer by id: {}", id);

        CustomerDto customer = customerService.getCustomerById(id);
        GenericResponse<CustomerDto> response = genericResponseService.createResponseNoError("Customer retrieved successfully", customer);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/code/{customerCode}")
    @Operation(summary = "Get customer by customer code")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ACCOUNTANT') or hasRole('MANAGER') or hasRole('VIEWER')")
    public ResponseEntity<GenericResponse<CustomerDto>> getCustomerByCode(
            @Parameter(description = "Customer code") @PathVariable String customerCode) {

        log.debug("Getting customer by code: {}", customerCode);

        CustomerDto customer = customerService.getCustomerByCode(customerCode);
        GenericResponse<CustomerDto> response = genericResponseService.createResponseNoError("Customer retrieved successfully", customer);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create new customer")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ACCOUNTANT')")
    public ResponseEntity<GenericResponse<CustomerDto>> createCustomer(
            @Parameter(description = "Customer data") @Valid @RequestBody CustomerDto customerDto) {

        log.debug("Creating new customer: {}", customerDto.getCustomerCode());

        CustomerDto createdCustomer = customerService.createCustomer(customerDto);
        GenericResponse<CustomerDto> response = genericResponseService.createResponseNoError("Customer created successfully", createdCustomer);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ACCOUNTANT')")
    public ResponseEntity<GenericResponse<CustomerDto>> updateCustomer(
            @Parameter(description = "Customer ID") @PathVariable Long id,
            @Parameter(description = "Updated customer data") @Valid @RequestBody CustomerDto customerDto) {

        log.debug("Updating customer with id: {}", id);

        CustomerDto updatedCustomer = customerService.updateCustomer(id, customerDto);
        GenericResponse<CustomerDto> response = genericResponseService.createResponseNoError("Customer updated successfully", updatedCustomer);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GenericResponse<Void>> deleteCustomer(
            @Parameter(description = "Customer ID") @PathVariable Long id) {

        log.debug("Deleting customer with id: {}", id);

        customerService.deleteCustomer(id);
        GenericResponse<Void> response = genericResponseService.createResponseNoError("Customer deleted successfully", null);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search")
    @Operation(summary = "Search customers")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ACCOUNTANT') or hasRole('MANAGER') or hasRole('VIEWER')")
    public ResponseEntity<GenericResponse<Page<CustomerDto>>> searchCustomers(
            @Parameter(description = "Search term") @RequestParam String searchTerm,
            @Parameter(description = "Page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "companyName") String sortBy,
            @Parameter(description = "Sort direction") @RequestParam(defaultValue = "asc") String sortDir) {

        log.debug("Searching customers with term: {}", searchTerm);

        Page<CustomerDto> customers = customerService.searchCustomers(searchTerm, page, size, sortBy, sortDir);
        GenericResponse<Page<CustomerDto>> response = genericResponseService.createResponseNoError("Customers found", customers);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get customers by status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ACCOUNTANT') or hasRole('MANAGER') or hasRole('VIEWER')")
    public ResponseEntity<GenericResponse<List<CustomerDto>>> getCustomersByStatus(
            @Parameter(description = "Customer status") @PathVariable CustomerStatus status) {

        log.debug("Getting customers by status: {}", status);

        List<CustomerDto> customers = customerService.getCustomersByStatus(status);
        GenericResponse<List<CustomerDto>> response = genericResponseService.createResponseNoError("Customers retrieved successfully", customers);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}