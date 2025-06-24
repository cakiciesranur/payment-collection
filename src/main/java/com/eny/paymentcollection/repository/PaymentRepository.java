package com.eny.paymentcollection.repository;

import com.eny.paymentcollection.enums.Currency;
import com.eny.paymentcollection.enums.PaymentStatus;
import com.eny.paymentcollection.enums.PaymentType;
import com.eny.paymentcollection.model.CustomerEntity;
import com.eny.paymentcollection.model.PaymentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    List<PaymentEntity> findByCustomer(CustomerEntity customer);

    Page<PaymentEntity> findByCustomer(CustomerEntity customer, Pageable pageable);

    List<PaymentEntity> findByStatus(PaymentStatus status);

    List<PaymentEntity> findByPaymentType(PaymentType paymentType);

    List<PaymentEntity> findByPaymentDateBetween(LocalDate startDate, LocalDate endDate);

    List<PaymentEntity> findByDueDateBefore(LocalDate date);

    List<PaymentEntity> findByStatusAndDueDateBefore(PaymentStatus status, LocalDate date);

    List<PaymentEntity> findByCustomerAndStatus(CustomerEntity customer, PaymentStatus status);

    List<PaymentEntity> findByAmountGreaterThan(BigDecimal amount);

    List<PaymentEntity> findByAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);

    List<PaymentEntity> findByCurrency(Currency currency);

    @Query("SELECT SUM(p.amount) FROM PaymentEntity p WHERE p.customer = :customer AND p.status = :status")
    BigDecimal sumAmountByCustomerAndStatus(@Param("customer") CustomerEntity customer,
                                            @Param("status") PaymentStatus status);

    long countByStatus(PaymentStatus status);

    long countByCustomer(CustomerEntity customer);
}