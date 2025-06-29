package com.eny.paymentcollection.service;

import com.eny.paymentcollection.dto.request.PaymentRequestDto;
import com.eny.paymentcollection.dto.response.PaymentResponseDto;
import com.eny.paymentcollection.enums.Currency;
import com.eny.paymentcollection.enums.PaymentStatus;
import com.eny.paymentcollection.enums.PaymentType;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface IPaymentService {

    Page<PaymentResponseDto> getAllPayments(int page, int size, String sortBy, String sortDir);

    PaymentResponseDto getPaymentById(Long id);

    Page<PaymentResponseDto> getPaymentsByCustomer(Long customerId, int page, int size, String sortBy, String sortDir);

    PaymentResponseDto createPayment(PaymentRequestDto request);

    PaymentResponseDto updatePayment(Long id, PaymentRequestDto request);

    PaymentResponseDto updatePaymentStatus(Long id, PaymentStatus status);

    void deletePayment(Long id);

    List<PaymentResponseDto> getPaymentsByStatus(PaymentStatus status);

    List<PaymentResponseDto> getPaymentsByDateRange(LocalDate start, LocalDate end);

    List<PaymentResponseDto> getOverduePayments();

    List<PaymentResponseDto> getPaymentsByType(PaymentType type);

    List<PaymentResponseDto> getPaymentsByCurrency(Currency currency);
}
