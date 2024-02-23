package com.picpay.challenge.api.exceptionhandler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ApiError {

    private HttpStatus status;
    private String resumo;
    private List<String> errors;

    public static ApiError of(final HttpStatus status,
                              final String resume,
                              final List<String> errorMessages) {
        final ApiError apiError = new ApiError(
                status,
                resume,
                errorMessages
        );

        return apiError;
    }

}
