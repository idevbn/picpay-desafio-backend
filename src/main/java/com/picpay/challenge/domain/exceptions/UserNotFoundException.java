package com.picpay.challenge.domain.exceptions;

import com.picpay.challenge.api.exceptionhandler.HttpException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UserNotFoundException extends RuntimeException implements HttpException {

    private final Long id;

    public UserNotFoundException(final Long id) {
        super();
        this.id = id;
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
