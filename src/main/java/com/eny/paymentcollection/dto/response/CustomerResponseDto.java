package com.eny.paymentcollection.dto.response;

import com.eny.paymentcollection.enums.CustomerStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Customer response data")
public class CustomerResponseDto {

    private Long id;
    private String customerCode;
    private String companyName;
    private String contactPerson;
    private String email;
    private String phone;
    private String address;
    private String taxNumber;
    private CustomerStatus status;

    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
    private String createdBy;
    private String lastModifiedBy;
}
