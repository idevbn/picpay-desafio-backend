package com.picpay.challenge.domain.exceptions;

import com.picpay.challenge.api.exceptionhandler.HttpException;
import org.springframework.http.HttpStatus;

public class TransactionNotAllowedException extends RuntimeException implements HttpException {

    @Override
    public String getErrorCode() {
        return "picpay.api.exceptions.transaction.shopkeeper.not-allowed";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public Object[] getErrorMessageParams() {
        return new String[]{""};
    }

}
