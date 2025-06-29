package com.eny.paymentcollection.controller;

import com.eny.paymentcollection.dto.request.LoginRequestDto;
import com.eny.paymentcollection.dto.request.SignUpDto;
import com.eny.paymentcollection.dto.response.GenericResponse;
import com.eny.paymentcollection.dto.response.LoginResponseDto;
import com.eny.paymentcollection.dto.response.UserResponseDto;
import com.eny.paymentcollection.service.GenericResponseService;
import com.eny.paymentcollection.service.IAuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/auth")
@RestController
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for user authentication")
public class AuthenticationController {
    private final IAuthenticationService authenticateService;
    private final GenericResponseService genericResponseService;

    @PostMapping("/register")
    @Operation(description = "This creates a new user")
    @ApiResponses
    public ResponseEntity<GenericResponse<UserResponseDto>> registerUser(@Valid @RequestBody SignUpDto signUpDto) {
        UserResponseDto registeredUser = authenticateService.register(signUpDto);
        GenericResponse<UserResponseDto> response = genericResponseService.createSuccessResponse("User registered successfully.", registeredUser);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user and return JWT token")
    public ResponseEntity<GenericResponse<LoginResponseDto>> authenticateUser(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = authenticateService.login(loginRequestDto);
        GenericResponse<LoginResponseDto> response = genericResponseService.createSuccessResponse("User logged in successfully", loginResponseDto);

        return ResponseEntity.ok(response);
    }
}