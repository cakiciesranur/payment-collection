package com.eny.paymentcollection.repository;

import com.eny.paymentcollection.enums.CustomerStatus;
import com.eny.paymentcollection.model.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

    Optional<CustomerEntity> findByCustomerCode(String customerCode);

    Optional<CustomerEntity> findByEmail(String email);

    List<CustomerEntity> findByStatus(CustomerStatus status);

    Page<CustomerEntity> findByCompanyNameContainingIgnoreCase(String companyName, Pageable pageable);

    Page<CustomerEntity> findByCompanyNameContainingIgnoreCaseOrCustomerCodeContainingIgnoreCase(
            String companyName, String customerCode, Pageable pageable);

    List<CustomerEntity> findByStatusAndCompanyNameContainingIgnoreCase(
            CustomerStatus status, String companyName);

    boolean existsByCustomerCode(String customerCode);

    boolean existsByEmail(String email);

    long countByStatus(CustomerStatus status);
}