package com.picpay.challenge.domain.exceptions;

import com.picpay.challenge.api.exceptionhandler.HttpException;
import org.springframework.http.HttpStatus;

public class TransactionNotAuthorizedException extends RuntimeException implements HttpException {

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public Object[] getErrorMessageParams() {
        return new String[]{""};
    }

}
