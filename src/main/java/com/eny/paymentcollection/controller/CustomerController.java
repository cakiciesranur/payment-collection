package com.eny.paymentcollection.controller;

import com.eny.paymentcollection.dto.request.CustomerRequestDto;
import com.eny.paymentcollection.dto.response.CustomerResponseDto;
import com.eny.paymentcollection.dto.response.GenericResponse;
import com.eny.paymentcollection.enums.CustomerStatus;
import com.eny.paymentcollection.service.GenericResponseService;
import com.eny.paymentcollection.service.ICustomerService;
import examples.customer.CustomerOperationExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
@RequestMapping("/api/customer")
@RequiredArgsConstructor
@Slf4j
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Customer Management", description = "Customer operations")
public class CustomerController {
    private final ICustomerService customerService;
    private final GenericResponseService genericResponseService;

    @GetMapping
    @Operation(summary = "Get all customers with pagination")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_ACCOUNTANT') or hasAuthority('ROLE_MANAGER') or hasAuthority('ROLE_VIEWER')")
    public ResponseEntity<GenericResponse<Page<CustomerResponseDto>>> getAllCustomers(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field") @RequestParam(defaultValue = "companyName") String sortBy,
            @Parameter(description = "Sort direction (asc or desc)") @RequestParam(defaultValue = "asc") String sortDir) {

        Page<CustomerResponseDto> customers = customerService.getAllCustomers(page, size, sortBy, sortDir);
        return ResponseEntity.ok(genericResponseService.createSuccessResponse("Customers retrieved successfully", customers));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_ACCOUNTANT') or hasAuthority('ROLE_MANAGER') or hasAuthority('ROLE_VIEWER')")
    public ResponseEntity<GenericResponse<CustomerResponseDto>> getCustomerById(
            @Parameter(description = "Customer ID") @PathVariable Long id) {

        CustomerResponseDto customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(genericResponseService.createSuccessResponse("Customer retrieved successfully", customer));
    }

    @GetMapping("/code/{customerCode}")
    @Operation(summary = "Get customer by customer code")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_ACCOUNTANT') or hasAuthority('ROLE_MANAGER') or hasAuthority('ROLE_VIEWER')")
    public ResponseEntity<GenericResponse<CustomerResponseDto>> getCustomerByCode(
            @Parameter(description = "Customer code") @PathVariable String customerCode) {

        CustomerResponseDto customer = customerService.getCustomerByCode(customerCode);
        return ResponseEntity.ok(genericResponseService.createSuccessResponse("Customer retrieved successfully", customer));
    }

    @PostMapping
    @Operation(summary = "Create a new customer",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = CustomerRequestDto.class),
                            examples = @ExampleObject(name = "Sample Customer", value = CustomerOperationExamples.CREATE_CUSTOMER_REQUEST_EXAMPLE)
                    )
            )
    )
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_ACCOUNTANT')")
    public ResponseEntity<GenericResponse<CustomerResponseDto>> createCustomer(
            @Parameter(description = "Customer data") @Valid @RequestBody CustomerRequestDto customerRequestDto) {

        CustomerResponseDto createdCustomer = customerService.createCustomer(customerRequestDto);
        return ResponseEntity.ok(genericResponseService.createSuccessResponse("Customer created successfully", createdCustomer));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update existing customer")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_ACCOUNTANT')")
    public ResponseEntity<GenericResponse<CustomerResponseDto>> updateCustomer(
            @Parameter(description = "Customer ID") @PathVariable Long id,
            @Parameter(description = "Updated customer data") @Valid @RequestBody CustomerRequestDto customerRequestDto) {

        CustomerResponseDto updated = customerService.updateCustomer(id, customerRequestDto);
        return ResponseEntity.ok(genericResponseService.createSuccessResponse("Customer updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a customer")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<GenericResponse<Void>> deleteCustomer(
            @Parameter(description = "Customer ID") @PathVariable Long id) {

        customerService.deleteCustomer(id);
        return ResponseEntity.ok(genericResponseService.createSuccessResponse("Customer deleted successfully", null));
    }

    @GetMapping("/search")
    @Operation(summary = "Search customers by name or code")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_ACCOUNTANT') or hasAuthority('ROLE_MANAGER') or hasAuthority('ROLE_VIEWER')")
    public ResponseEntity<GenericResponse<Page<CustomerResponseDto>>> searchCustomers(
            @Parameter(description = "Search term") @RequestParam String searchTerm,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "companyName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Page<CustomerResponseDto> customers = customerService.searchCustomers(searchTerm, page, size, sortBy, sortDir);
        return ResponseEntity.ok(genericResponseService.createSuccessResponse("Customers found", customers));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Get customers by status")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_ACCOUNTANT') or hasAuthority('ROLE_MANAGER') or hasAuthority('ROLE_VIEWER')")
    public ResponseEntity<GenericResponse<List<CustomerResponseDto>>> getCustomersByStatus(
            @Parameter(description = "Customer status") @PathVariable CustomerStatus status) {

        List<CustomerResponseDto> customers = customerService.getCustomersByStatus(status);
        return ResponseEntity.ok(genericResponseService.createSuccessResponse("Customers by status retrieved successfully", customers));
    }

    @GetMapping("/exists/{customerCode}")
    @Operation(summary = "Check if a customer exists by customer code")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_ACCOUNTANT')")
    public ResponseEntity<GenericResponse<Boolean>> existsByCustomerCode(
            @Parameter(description = "Customer code") @PathVariable String customerCode) {

        boolean exists = customerService.existsByCustomerCode(customerCode);
        return ResponseEntity.ok(genericResponseService.createSuccessResponse("Customer existence checked", exists));
    }
}
