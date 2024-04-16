package com.picpay.challenge.domain.exceptions;

import com.picpay.challenge.api.exceptionhandler.HttpException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserNotFoundException extends RuntimeException implements HttpException {

    private final Long id;

    public UserNotFoundException(final Long id) {
        this.id = id;
    }

    @Override
    public String getErrorCode() {
        return "picpay.api.exceptions.user.not-found";
    }

    @Override
    public HttpStatus getHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public Object[] getErrorMessageParams() {
        return new String[]{this.getId().toString()};
    }

}
