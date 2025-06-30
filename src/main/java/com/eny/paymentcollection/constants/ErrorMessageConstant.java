package com.eny.paymentcollection.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
public class ErrorMessageConstant {
    public static final int AUTHENTICATION_ERROR = 1;
    public static final int PARSE_ERROR = 2;
    public static final int PROCESS_ERROR = 3;
    public static final int JWT_EXPIRED_ERROR = 4;
    public static final int BAD_CREDENTIALS_ERROR = 5;
    public static final int USERNAME_EXIST = 6;
    public static final int NOT_FOUND = 404;
    public static final int AUTHORIZATION_ERROR = 401;
    public static final int UNKNOWN_ERROR = 999;
    public static final int CUSTOMER_NOT_FOUND = 1001;
    public static final int PAYMENT_NOT_FOUND = 1002;
    public static final int DUPLICATE_CUSTOMER_CODE = 1003;
    public static final int INVALID_PAYMENT_AMOUNT = 1004;
    public static final int PAYMENT_ALREADY_COMPLETED = 1005;
}