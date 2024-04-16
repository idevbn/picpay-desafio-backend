package com.picpay.challenge.api.exceptionhandler;

import org.springframework.http.HttpStatus;

public interface HttpException {

    String getErrorCode();

    HttpStatus getHttpStatus();

    Object[] getErrorMessageParams();

}
