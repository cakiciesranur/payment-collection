package com.eny.paymentcollection.service.impl;

import com.eny.paymentcollection.dto.request.CustomerRequestDto;
import com.eny.paymentcollection.dto.response.CustomerResponseDto;
import com.eny.paymentcollection.enums.CustomerStatus;
import com.eny.paymentcollection.exception.DuplicateResourceException;
import com.eny.paymentcollection.exception.ResourceNotFoundException;
import com.eny.paymentcollection.mapper.CustomerMapper;
import com.eny.paymentcollection.model.CustomerEntity;
import com.eny.paymentcollection.repository.CustomerRepository;
import com.eny.paymentcollection.service.ICustomerService;
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
public class CustomerServiceImpl implements ICustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponseDto> getAllCustomers(int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        return customerRepository.findAll(pageable).map(customerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CustomerResponseDto> searchCustomers(String searchTerm, int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        return customerRepository
                .findByCompanyNameContainingIgnoreCaseOrCustomerCodeContainingIgnoreCase(searchTerm, searchTerm, pageable)
                .map(customerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDto getCustomerById(Long id) {
        CustomerEntity customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return customerMapper.toDto(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDto getCustomerByCode(String customerCode) {
        CustomerEntity customer = customerRepository.findByCustomerCode(customerCode)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with code: " + customerCode));
        return customerMapper.toDto(customer);
    }

    @Override
    public CustomerResponseDto createCustomer(CustomerRequestDto customerDto) {
        validateCustomerForCreation(customerDto);

        CustomerEntity entity = customerMapper.toEntity(customerDto);
        entity.setStatus(CustomerStatus.ACTIVE);

        CustomerEntity savedCustomer = customerRepository.save(entity);
        log.info("Customer created successfully with id: {}", savedCustomer.getId());

        return customerMapper.toDto(savedCustomer);
    }

    @Override
    public CustomerResponseDto updateCustomer(Long id, CustomerRequestDto customerRequest) {
        CustomerEntity existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        validateCustomerForUpdate(customerRequest, id);

        customerMapper.updateEntityFromDto(customerRequest, existingCustomer);

        CustomerEntity updatedCustomer = customerRepository.save(existingCustomer);
        log.info("Customer updated successfully with id: {}", updatedCustomer.getId());

        return customerMapper.toDto(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long id) {
        CustomerEntity entity = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));

        customerRepository.delete(entity);
        log.info("Customer deleted successfully with id: {}", id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CustomerResponseDto> getCustomersByStatus(CustomerStatus status) {
        return customerMapper.toDtoList(customerRepository.findByStatus(status));
    }

    @Override
    public boolean existsByCustomerCode(String customerCode) {
        return customerRepository.existsByCustomerCode(customerCode);
    }

    private void validateCustomerForCreation(CustomerRequestDto customerRequest) {
        if (customerRepository.existsByCustomerCode(customerRequest.getCustomerCode())) {
            throw new DuplicateResourceException("Customer code already exists: " + customerRequest.getCustomerCode());
        }

        if (customerRequest.getEmail() != null && customerRepository.existsByEmail(customerRequest.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + customerRequest.getEmail());
        }
    }

    private void validateCustomerForUpdate(CustomerRequestDto customerDto, Long customerId) {
        customerRepository.findByCustomerCode(customerDto.getCustomerCode())
                .ifPresent(existing -> {
                    if (!existing.getId().equals(customerId)) {
                        throw new DuplicateResourceException("Customer code already exists: " + customerDto.getCustomerCode());
                    }
                });

        if (customerDto.getEmail() != null) {
            customerRepository.findByEmail(customerDto.getEmail())
                    .ifPresent(existing -> {
                        if (!existing.getId().equals(customerId)) {
                            throw new DuplicateResourceException("Email already exists: " + customerDto.getEmail());
                        }
                    });
        }
    }
}