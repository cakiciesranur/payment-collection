package com.eny.paymentcollection.service.impl;

import com.eny.paymentcollection.dto.request.LoginRequestDto;
import com.eny.paymentcollection.dto.request.SignUpDto;
import com.eny.paymentcollection.dto.response.LoginResponseDto;
import com.eny.paymentcollection.dto.response.UserResponseDto;
import com.eny.paymentcollection.security.jwt.JwtUtil;
import com.eny.paymentcollection.service.IAuthenticationService;
import com.eny.paymentcollection.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements IAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final IUserService userService;

    @Override
    public UserResponseDto register(SignUpDto request) {
        return userService.createUser(request);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(authentication);

        return new LoginResponseDto(jwt);
    }

    @Override
    public UserResponseDto getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userService.getByUsername(username);
    }
}
