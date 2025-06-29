package com.eny.paymentcollection.controller;

import com.eny.paymentcollection.dto.request.UpdateUserDto;
import com.eny.paymentcollection.dto.response.GenericResponse;
import com.eny.paymentcollection.dto.response.UserResponseDto;
import com.eny.paymentcollection.service.GenericResponseService;
import com.eny.paymentcollection.service.IAuthenticationService;
import com.eny.paymentcollection.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "User Management", description = "User operations")
public class UserController {
    private final IUserService userService;
    private final IAuthenticationService authenticationService;
    private final GenericResponseService genericResponseService;

    //TODO: use patch
    @PostMapping("/updateUser")
    @Operation(description = "This updates an existing user")
    public ResponseEntity<GenericResponse<UserResponseDto>> updateUser(@Valid @RequestBody UpdateUserDto request) {
        UserResponseDto newUser = userService.updateUser(request);
        return ResponseEntity.ok(genericResponseService.createSuccessResponse("Updated user successfully!", newUser));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(description = "This gives a list of all users")
    public ResponseEntity<GenericResponse<List<UserResponseDto>>> getUsers() {
        List<UserResponseDto> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(genericResponseService.createSuccessResponse("Fetched all users successfully", allUsers));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current authenticated user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<GenericResponse<UserResponseDto>> getCurrentUser() {
        UserResponseDto userDto = authenticationService.getCurrentUser();
        GenericResponse<UserResponseDto> response = genericResponseService.createSuccessResponse("Current user retrieved", userDto);

        return ResponseEntity.ok(response);
    }
}
