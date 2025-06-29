package com.eny.paymentcollection.service.impl;

import com.eny.paymentcollection.dto.request.PaymentRequestDto;
import com.eny.paymentcollection.dto.response.PaymentResponseDto;
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
import com.eny.paymentcollection.service.IPaymentService;
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
public class PaymentServiceImpl implements IPaymentService {

    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentResponseDto> getAllPayments(int page, int size, String sortBy, String sortDir) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortDir), sortBy));
        return paymentRepository.findAll(pageable)
                .map(paymentMapper::toResponseDto);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentById(Long id) {
        PaymentEntity entity = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        return paymentMapper.toResponseDto(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentResponseDto> getPaymentsByCustomer(Long customerId, int page, int size, String sortBy, String sortDir) {
        CustomerEntity customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PaymentEntity> paymentPage = paymentRepository.findByCustomer(customer, pageable);

        return paymentPage.map(paymentMapper::toResponseDto);
    }

    @Override
    public PaymentResponseDto createPayment(PaymentRequestDto dto) {
        validate(dto);

        CustomerEntity customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        PaymentEntity entity = paymentMapper.toEntity(dto);
        entity.setCustomer(customer);
        entity.setStatus(PaymentStatus.PENDING);

        return paymentMapper.toResponseDto(paymentRepository.save(entity));
    }

    @Override
    public PaymentResponseDto updatePayment(Long id, PaymentRequestDto dto) {
        PaymentEntity existing = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (existing.getStatus() == PaymentStatus.COMPLETED) {
            throw new NotAllowedOperationException("Cannot update a completed payment");
        }

        validate(dto);

        CustomerEntity customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        paymentMapper.updateEntityFromDto(dto, existing);
        existing.setCustomer(customer);

        return paymentMapper.toResponseDto(paymentRepository.save(existing));
    }

    @Override
    public PaymentResponseDto updatePaymentStatus(Long id, PaymentStatus status) {
        PaymentEntity entity = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        entity.setStatus(status);
        return paymentMapper.toResponseDto(paymentRepository.save(entity));
    }

    @Override
    public void deletePayment(Long id) {
        PaymentEntity entity = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        if (entity.getStatus() == PaymentStatus.COMPLETED) {
            throw new NotAllowedOperationException("Cannot delete a completed payment");
        }

        paymentRepository.delete(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getPaymentsByStatus(PaymentStatus status) {
        return paymentMapper.toResponseDtoList(paymentRepository.findByStatus(status));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getPaymentsByDateRange(LocalDate start, LocalDate end) {
        return paymentMapper.toResponseDtoList(paymentRepository.findByPaymentDateBetween(start, end));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getOverduePayments() {
        return paymentMapper.toResponseDtoList(paymentRepository.findByStatusAndDueDateBefore(PaymentStatus.PENDING, LocalDate.now()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getPaymentsByType(PaymentType type) {
        return paymentMapper.toResponseDtoList(paymentRepository.findByPaymentType(type));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getPaymentsByCurrency(Currency currency) {
        return paymentMapper.toResponseDtoList(paymentRepository.findByCurrency(currency));
    }

    private void validate(PaymentRequestDto dto) {
        if (dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new NotAllowedOperationException("Amount must be greater than zero");
        }
        if (dto.getDueDate() != null && dto.getDueDate().isBefore(dto.getPaymentDate())) {
            throw new NotAllowedOperationException("Due date cannot be before payment date");
        }
        if (dto.getPaymentDate().isAfter(LocalDate.now())) {
            throw new NotAllowedOperationException("Payment date cannot be in the future");
        }
    }
}
