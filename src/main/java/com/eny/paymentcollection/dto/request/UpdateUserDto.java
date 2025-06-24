package com.eny.paymentcollection.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "Update User Transfer Object")
public class UpdateUserDto implements Serializable {
    private static final long serialVersionUID = -5864930635003583949L;

    @Schema(description = "name field")
    @Size(min = 4, max = 40)
    private String name;

    @Schema(description = "username field")
    @Size(min = 3, max = 15)
    private String username;

    @Schema(description = "email field")
    @Size(max = 40)
    @Email
    private String email;
}
