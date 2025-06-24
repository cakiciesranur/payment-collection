package com.eny.paymentcollection.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Schema(description = "Login request payload")
public class LoginRequestDto {
    @NotBlank
    @Schema(description = "Username of the user", example = "john_doe")
    private String usernameOrEmail;

    @NotBlank
    @Schema(description = "Password of the user", example = "password123")
    private String password;
}