package com.eny.paymentcollection.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@Schema(description = "SignUp Data Transfer Object")
public class SignUpDto implements Serializable {

    private static final long serialVersionUID = 309271832375484192L;

    @Schema(description = "User's full name")
    @NotBlank
    @Size(min = 4, max = 40)
    private String name;

    @Schema(description = "Username (unique)")
    @NotBlank
    @Size(min = 3, max = 15)
    private String username;

    @Schema(description = "Email address")
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @Schema(description = "Password")
    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    @Schema(description = "Optional set of roles, e.g. ['ROLE_USER', 'ROLE_ADMIN']")
    private Set<String> roles; // optional
}
