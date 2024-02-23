package com.picpay.challenge.api.exceptionhandler;

import com.picpay.challenge.domain.exceptions.InsufficientBalanceException;
import com.picpay.challenge.domain.exceptions.TransactionNotAllowedException;
import com.picpay.challenge.domain.exceptions.TransactionNotAuthorizedException;
import com.picpay.challenge.domain.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    @Autowired
    public ApiExceptionHandler(final MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(final UserNotFoundException ex) {
        logger.info(ex);

        final String errorMsg = this.messageSource.getMessage(
                "picpay.api.exceptions.user.not-found",
                ex.getErrorMessageParams(),
                LocaleContextHolder.getLocale()
        );

        final ApiError apiError = ApiError.of(
                ex.getHttpStatus(),
                errorMsg,
                Collections.singletonList(ex.toString())
        );

        return ResponseEntity.status(ex.getHttpStatus()).body(apiError);
    }

    @ExceptionHandler(TransactionNotAllowedException.class)
    public ResponseEntity<ApiError> handleTransactionNotAllowedException(
            final TransactionNotAllowedException ex
    ) {
        logger.info(ex);

        final String errorMsg = this.messageSource.getMessage(
                "picpay.api.exceptions.transaction.shopkeeper.not-allowed",
                ex.getErrorMessageParams(),
                LocaleContextHolder.getLocale()
        );

        final ApiError apiError = ApiError.of(
                ex.getHttpStatus(),
                errorMsg,
                Collections.singletonList(ex.toString())
        );

        return ResponseEntity.status(ex.getHttpStatus()).body(apiError);
    }

    @ExceptionHandler(TransactionNotAuthorizedException.class)
    public ResponseEntity<ApiError> handleTransactionNotAuthorizedException(
            final TransactionNotAuthorizedException ex
    ) {
        logger.info(ex);

        final String errorMsg = this.messageSource.getMessage(
                "picpay.api.exceptions.transaction.not-authorized",
                ex.getErrorMessageParams(),
                LocaleContextHolder.getLocale()
        );

        final ApiError apiError = ApiError.of(
                ex.getHttpStatus(),
                errorMsg,
                Collections.singletonList(ex.toString())
        );

        return ResponseEntity.status(ex.getHttpStatus()).body(apiError);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ApiError> handleInsufficientBalanceException(
            final InsufficientBalanceException ex
    ) {
        logger.info(ex);

        final String errorMsg = this.messageSource.getMessage(
                "picpay.api.exceptions.transaction.balance.insufficient",
                ex.getErrorMessageParams(),
                LocaleContextHolder.getLocale()
        );

        final ApiError apiError = ApiError.of(
                ex.getHttpStatus(),
                errorMsg,
                Collections.singletonList(ex.toString())
        );

        return ResponseEntity.status(ex.getHttpStatus()).body(apiError);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolationException(
            final DataIntegrityViolationException ex
    ) {
        logger.info(ex);

        final HttpStatus badRequestStatus = HttpStatus.BAD_REQUEST;

        final String errorMsg = this.messageSource.getMessage(
                "picpay.api.exceptions.user.data-integrity",
                ex.getStackTrace(),
                LocaleContextHolder.getLocale()
        );

        final ApiError apiError = ApiError.of(
                badRequestStatus,
                errorMsg,
                Collections.singletonList(ex.toString())
        );

        return ResponseEntity.status(badRequestStatus).body(apiError);
    }

}
