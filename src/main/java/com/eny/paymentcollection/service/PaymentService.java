package com.eny.paymentcollection.service;

import com.eny.paymentcollection.dto.request.PaymentDto;
import com.eny.paymentcollection.enums.Currency;
import com.eny.paymentcollection.enums.PaymentStatus;
import com.eny.paymentcollection.enums.PaymentType;
import com.eny.paymentcollection.exception.NotAllowedOperationException;
import com.eny.paymentcollection.exception.ResourceNotFoundException;
import com.eny.paymentcollection.mapper.PaymentMapper;
import com.eny.paymentcollection.model.CustomerEntity;
import com.eny.paymentcollection.model.PaymentEntity;
import com.eny.paymentcollection.repository.CustomerRepository;
import com.eny.paymentcollection.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;
    private final PaymentMapper paymentMapper;

    @Transactional(readOnly = true)
    public Page<PaymentDto> getAllPayments(int page, int size, String sortBy, String sortDir) {
        log.debug("Getting all payments - page: {}, size: {}, sortBy: {}, sortDir: {}",
                page, size, sortBy, sortDir);

        Sort sort = createSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<PaymentEntity> paymentPage = paymentRepository.findAll(pageable);
        return paymentPage.map(paymentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<PaymentDto> getAllPayments(Pageable pageable) {
        Page<PaymentEntity> paymentPage = paymentRepository.findAll(pageable);
        return paymentPage.map(paymentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public PaymentDto getPaymentById(Long id) {
        log.debug("Getting payment by id: {}", id);
        PaymentEntity payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        return paymentMapper.toDto(payment);
    }

    @Transactional(readOnly = true)
    public Page<PaymentDto> getPaymentsByCustomer(Long customerId, int page, int size, String sortBy, String sortDir) {
        log.debug("Getting payments for customer id: {} - page: {}, size: {}", customerId, page, size);

        CustomerEntity customer = findCustomerById(customerId);
        Sort sort = createSort(sortBy, sortDir);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<PaymentEntity> paymentPage = paymentRepository.findByCustomer(customer, pageable);
        return paymentPage.map(paymentMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<PaymentDto> getPaymentsByCustomer(Long customerId, Pageable pageable) {
        CustomerEntity customer = findCustomerById(customerId);
        Page<PaymentEntity> paymentPage = paymentRepository.findByCustomer(customer, pageable);
        return paymentPage.map(paymentMapper::toDto);
    }

    public PaymentDto createPayment(PaymentDto paymentDto) {
        log.debug("Creating new payment for customer id: {}", paymentDto.getCustomerId());

        validatePaymentForCreation(paymentDto);

        CustomerEntity customer = findCustomerById(paymentDto.getCustomerId());

        PaymentEntity payment = paymentMapper.toEntity(paymentDto);
        payment.setCustomer(customer);
        payment.setStatus(PaymentStatus.PENDING);

        PaymentEntity savedPayment = paymentRepository.save(payment);
        log.info("Payment created successfully with id: {}", savedPayment.getId());

        return paymentMapper.toDto(savedPayment);
    }

    public PaymentDto updatePayment(Long id, PaymentDto paymentDto) {
        log.debug("Updating payment with id: {}", id);

        PaymentEntity existingPayment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        validatePaymentForUpdate(paymentDto, existingPayment);

        CustomerEntity customer = findCustomerById(paymentDto.getCustomerId());

        paymentMapper.updateEntityFromDto(paymentDto, existingPayment);
        existingPayment.setCustomer(customer);

        PaymentEntity updatedPayment = paymentRepository.save(existingPayment);
        log.info("Payment updated successfully with id: {}", updatedPayment.getId());

        return paymentMapper.toDto(updatedPayment);
    }

    public PaymentDto updatePaymentStatus(Long id, PaymentStatus status) {
        log.debug("Updating payment status with id: {} to status: {}", id, status);

        PaymentEntity payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        payment.setStatus(status);
        PaymentEntity updatedPayment = paymentRepository.save(payment);
        log.info("Payment status updated successfully with id: {}", updatedPayment.getId());

        return paymentMapper.toDto(updatedPayment);
    }

    public void deletePayment(Long id) {
        log.debug("Deleting payment with id: {}", id);

        PaymentEntity payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));

        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            throw new NotAllowedOperationException("Cannot delete completed payment");
        }

        paymentRepository.delete(payment);
        log.info("Payment deleted successfully with id: {}", id);
    }

    @Transactional(readOnly = true)
    public List<PaymentDto> getPaymentsByStatus(PaymentStatus status) {
        log.debug("Getting payments by status: {}", status);
        List<PaymentEntity> payments = paymentRepository.findByStatus(status);
        return paymentMapper.toDtoList(payments);
    }

    @Transactional(readOnly = true)
    public List<PaymentDto> getPaymentsByDateRange(LocalDate startDate, LocalDate endDate) {
        log.debug("Getting payments between dates: {} and {}", startDate, endDate);
        List<PaymentEntity> payments = paymentRepository.findByPaymentDateBetween(startDate, endDate);
        return paymentMapper.toDtoList(payments);
    }

    @Transactional(readOnly = true)
    public List<PaymentDto> getOverduePayments() {
        log.debug("Getting overdue payments");
        LocalDate today = LocalDate.now();
        List<PaymentEntity> payments = paymentRepository.findByStatusAndDueDateBefore(PaymentStatus.PENDING, today);
        return paymentMapper.toDtoList(payments);
    }

    @Transactional(readOnly = true)
    public List<PaymentDto> getPaymentsByType(PaymentType paymentType) {
        log.debug("Getting payments by type: {}", paymentType);
        List<PaymentEntity> payments = paymentRepository.findByPaymentType(paymentType);
        return paymentMapper.toDtoList(payments);
    }

    @Transactional(readOnly = true)
    public List<PaymentDto> getPaymentsByCurrency(Currency currency) {
        log.debug("Getting payments by currency: {}", currency);
        List<PaymentEntity> payments = paymentRepository.findByCurrency(currency);
        return paymentMapper.toDtoList(payments);
    }

    // Utility method for creating Sort object
    private Sort createSort(String sortBy, String sortDir) {
        return sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();
    }

    private CustomerEntity findCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
    }

    private void validatePaymentForCreation(PaymentDto paymentDto) {
        if (paymentDto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new NotAllowedOperationException("Payment amount must be greater than zero");
        }

        if (paymentDto.getPaymentDate().isAfter(LocalDate.now())) {
            throw new NotAllowedOperationException("Payment date cannot be in the future");
        }

        if (paymentDto.getDueDate() != null && paymentDto.getDueDate().isBefore(paymentDto.getPaymentDate())) {
            throw new NotAllowedOperationException("Due date cannot be before payment date");
        }
    }

    private void validatePaymentForUpdate(PaymentDto paymentDto, PaymentEntity existingPayment) {
        if (existingPayment.getStatus() == PaymentStatus.COMPLETED) {
            throw new NotAllowedOperationException("Cannot update completed payment");
        }

        validatePaymentForCreation(paymentDto);
    }
}