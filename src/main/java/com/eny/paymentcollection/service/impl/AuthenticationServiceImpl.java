package com.eny.paymentcollection.service.impl;

import com.eny.paymentcollection.dto.response.GenericResponse;
import com.eny.paymentcollection.dto.response.JwtAuthenticationResponse;
import com.eny.paymentcollection.security.jwt.JwtUtil;
import com.eny.paymentcollection.service.GenericResponseService;
import com.eny.paymentcollection.service.IAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements IAuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    private GenericResponseService genericResponseService;

    @Override
    public GenericResponse login(String usernameOrEmail, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usernameOrEmail, password));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(authentication);

        return genericResponseService.createResponseNoError("", new JwtAuthenticationResponse(jwt));
    }
}
