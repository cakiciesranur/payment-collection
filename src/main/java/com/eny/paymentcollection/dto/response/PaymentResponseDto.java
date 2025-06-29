package com.eny.paymentcollection.dto.response;

import com.eny.paymentcollection.enums.Currency;
import com.eny.paymentcollection.enums.PaymentStatus;
import com.eny.paymentcollection.enums.PaymentType;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PaymentResponseDto {

    private Long id;

    private Long customerId;
    private String customerCode;
    private String customerCompanyName;

    private BigDecimal amount;
    private Currency currency;

    private LocalDate paymentDate;
    private LocalDate dueDate;

    private PaymentType paymentType;
    private PaymentStatus status;

    private String notes;

    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    private String createdBy;
    private String lastModifiedBy;
}
