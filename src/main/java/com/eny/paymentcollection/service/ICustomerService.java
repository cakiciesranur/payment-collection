package com.eny.paymentcollection.service;

import com.eny.paymentcollection.dto.request.CustomerRequestDto;
import com.eny.paymentcollection.dto.response.CustomerResponseDto;
import com.eny.paymentcollection.enums.CustomerStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ICustomerService {

    Page<CustomerResponseDto> getAllCustomers(int page, int size, String sortBy, String sortDir);

    Page<CustomerResponseDto> searchCustomers(String searchTerm, int page, int size, String sortBy, String sortDir);

    CustomerResponseDto getCustomerById(Long id);

    CustomerResponseDto getCustomerByCode(String customerCode);

    CustomerResponseDto createCustomer(CustomerRequestDto customerDto);

    CustomerResponseDto updateCustomer(Long id, CustomerRequestDto customerDto);

    void deleteCustomer(Long id);

    List<CustomerResponseDto> getCustomersByStatus(CustomerStatus status);

    boolean existsByCustomerCode(String customerCode);
}
