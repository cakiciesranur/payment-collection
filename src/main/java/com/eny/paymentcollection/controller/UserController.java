package com.eny.paymentcollection.controller;

import com.eny.paymentcollection.dto.request.SignUpDto;
import com.eny.paymentcollection.dto.request.UpdateUserDto;
import com.eny.paymentcollection.dto.response.GenericResponse;
import com.eny.paymentcollection.model.UserEntity;
import com.eny.paymentcollection.service.GenericResponseService;
import com.eny.paymentcollection.service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
public class UserController {
    private final IUserService userService;
    private final GenericResponseService genericResponseService;

    @PostMapping("/register")
    @Operation(description = "This creates a new user")
    public GenericResponse registerUser(@Valid @RequestBody SignUpDto signUpDto) {

        UserEntity user = userService.createUser(signUpDto);
        return genericResponseService.createResponseNoError("Created user successfully!", user);
    }

    //TODO: use patch
    //TODO: responseentity return??
    @PostMapping("/updateUser")
    @Operation(description = "This updates an existing user")
    public GenericResponse updateUser(@Valid @RequestBody UpdateUserDto request) {

        UserEntity newUser = userService.updateUser(request);
        return genericResponseService.createResponseNoError("Updated user successfully!", newUser);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(description = "This gives a list of all users")
    public GenericResponse getUsers() {
        List<UserEntity> allUsers = userService.getAllUsers();
        return genericResponseService.createResponseNoError("", allUsers);
    }
}
