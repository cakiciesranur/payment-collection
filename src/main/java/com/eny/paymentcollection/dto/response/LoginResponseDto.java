package com.eny.paymentcollection.dto.response;

import com.eny.paymentcollection.constants.AuthenticationConstants;
import lombok.Data;

@Data
public class LoginResponseDto {
    private String accessToken;
    private String tokenType = AuthenticationConstants.BEARER_TOKEN_TYPE;

    public LoginResponseDto(String accessToken) {
        this.accessToken = accessToken;
    }
}
