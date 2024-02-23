package com.picpay.challenge.api.exceptionhandler;

import org.springframework.http.HttpStatus;

public interface HttpException {

    HttpStatus getHttpStatus();

    Object[] getErrorMessageParams();

}
