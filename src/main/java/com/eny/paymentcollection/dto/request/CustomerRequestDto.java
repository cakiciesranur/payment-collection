package com.eny.paymentcollection.dto.request;

import com.eny.paymentcollection.enums.CustomerStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Customer request payload")
public class CustomerRequestDto {

    @NotBlank(message = "Customer code is required")
    @Size(max = 20, message = "Customer code must not exceed 20 characters")
    private String customerCode;

    @NotBlank(message = "Company name is required")
    @Size(max = 255, message = "Company name must not exceed 255 characters")
    private String companyName;

    @Size(max = 100, message = "Contact person must not exceed 100 characters")
    private String contactPerson;

    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    private String email;

    @Size(max = 20, message = "Phone must not exceed 20 characters")
    private String phone;

    @Size(max = 500, message = "Address must not exceed 500 characters")
    private String address;

    @Size(max = 20, message = "Tax number must not exceed 20 characters")
    private String taxNumber;

    private CustomerStatus status;
}
