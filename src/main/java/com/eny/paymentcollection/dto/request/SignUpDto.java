package com.eny.paymentcollection.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "SignUp Data Transfer Object")
public class SignUpDto implements Serializable {

    private static final long serialVersionUID = 309271832375484192L;

    @Schema(description = "name field")
    @NotBlank
    @Size(min = 4, max = 40)
    private String name;

    @Schema(description = "username field")
    @NotBlank
    @Size(min = 3, max = 15)
    private String username;

    @Schema(description = "email field")
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @Schema(description = "password field")
    @NotBlank
    @Size(min = 6, max = 20)
    private String password;
}
