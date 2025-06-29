package com.eny.paymentcollection.dto.request;

import com.eny.paymentcollection.enums.Currency;
import com.eny.paymentcollection.enums.PaymentType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PaymentRequestDto {

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    @Digits(integer = 13, fraction = 2, message = "Invalid amount format")
    private BigDecimal amount;

    private Currency currency;

    @NotNull(message = "Payment date is required")
    private LocalDate paymentDate;

    private LocalDate dueDate;

    private PaymentType paymentType;

    @Size(max = 500, message = "Notes must not exceed 500 characters")
    private String notes;
}
