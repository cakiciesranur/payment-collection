package com.eny.paymentcollection.service;

import com.eny.paymentcollection.dto.request.CustomerDto;
import com.eny.paymentcollection.enums.CustomerStatus;
import com.eny.paymentcollection.exception.DuplicateResourceException;
import com.eny.paymentcollection.exception.ResourceNotFoundException;
import com.eny.paymentcollection.mapper.CustomerMapper;
import com.eny.paymentcollection.model.CustomerEntity;
import com.eny.paymentcollection.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Transactional(readOnly = true)
    public Page<CustomerDto> getAllCustomers(int page, int size, String sortBy, String sortDir) {
        log.debug("Getting all customers - page: {}, size: {}, sortBy: {}, sortDir: {}",
                page, size, sortBy, sortDir);

        Sort sort = createSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CustomerEntity> customerPage = customerRepository.findAll(pageable);
        return customerPage.map(customerMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<CustomerDto> searchCustomers(String searchTerm, int page, int size, String sortBy, String sortDir) {
        log.debug("Searching customers with term: {}", searchTerm);

        Sort sort = createSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<CustomerEntity> customerPage = customerRepository
                .findByCompanyNameContainingIgnoreCaseOrCustomerCodeContainingIgnoreCase(
                        searchTerm, searchTerm, pageable);
        return customerPage.map(customerMapper::toDto);
    }

    @Transactional(readOnly = true)
    public CustomerDto getCustomerById(Long id) {
        log.debug("Getting customer by id: {}", id);
        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return customerMapper.toDto(customer);
    }

    @Transactional(readOnly = true)
    public CustomerDto getCustomerByCode(String customerCode) {
        log.debug("Getting customer by code: {}", customerCode);
        CustomerEntity customer = customerRepository.findByCustomerCode(customerCode)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with code: " + customerCode));
        return customerMapper.toDto(customer);
    }

    public CustomerDto createCustomer(CustomerDto customerDto) {
        log.debug("Creating new customer: {}", customerDto.getCustomerCode());

        validateCustomerForCreation(customerDto);

        CustomerEntity customer = customerMapper.toEntity(customerDto);
        customer.setStatus(CustomerStatus.ACTIVE);

        CustomerEntity savedCustomer = customerRepository.save(customer);
        log.info("Customer created successfully with id: {}", savedCustomer.getId());

        return customerMapper.toDto(savedCustomer);
    }

    public CustomerDto updateCustomer(Long id, CustomerDto customerDto) {
        log.debug("Updating customer with id: {}", id);

        CustomerEntity existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        validateCustomerForUpdate(customerDto, id);

        customerMapper.updateEntityFromDto(customerDto, existingCustomer);

        CustomerEntity updatedCustomer = customerRepository.save(existingCustomer);
        log.info("Customer updated successfully with id: {}", updatedCustomer.getId());

        return customerMapper.toDto(updatedCustomer);
    }

    public void deleteCustomer(Long id) {
        log.debug("Deleting customer with id: {}", id);

        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        customerRepository.delete(customer);
        log.info("Customer deleted successfully with id: {}", id);
    }

    @Transactional(readOnly = true)
    public Page<CustomerDto> searchCustomers(String searchTerm, Pageable pageable) {
        log.debug("Searching customers with term: {}", searchTerm);
        Page<CustomerEntity> customerPage = customerRepository
                .findByCompanyNameContainingIgnoreCaseOrCustomerCodeContainingIgnoreCase(
                        searchTerm, searchTerm, pageable);
        return customerPage.map(customerMapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<CustomerDto> getCustomersByStatus(CustomerStatus status) {
        log.debug("Getting customers by status: {}", status);
        List<CustomerEntity> customers = customerRepository.findByStatus(status);
        return customerMapper.toDtoList(customers);
    }

    private void validateCustomerForCreation(CustomerDto customerDto) {
        if (customerRepository.existsByCustomerCode(customerDto.getCustomerCode())) {
            throw new DuplicateResourceException("Customer code already exists: " + customerDto.getCustomerCode());
        }

        if (customerDto.getEmail() != null && customerRepository.existsByEmail(customerDto.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + customerDto.getEmail());
        }
    }

    private void validateCustomerForUpdate(CustomerDto customerDto, Long currentId) {
        customerRepository.findByCustomerCode(customerDto.getCustomerCode())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(currentId)) {
                        throw new DuplicateResourceException("Customer code already exists: " + customerDto.getCustomerCode());
                    }
                });

        if (customerDto.getEmail() != null) {
            customerRepository.findByEmail(customerDto.getEmail())
                    .ifPresent(existing -> {
                        if (!existing.getId().equals(currentId)) {
                            throw new DuplicateResourceException("Email already exists: " + customerDto.getEmail());
                        }
                    });
        }
    }

    // Utility method for creating Sort object
    private Sort createSort(String sortBy, String sortDir) {
        return sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();
    }
}