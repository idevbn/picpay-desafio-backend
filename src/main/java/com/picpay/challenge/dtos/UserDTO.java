package com.picpay.challenge.dtos;

import java.math.BigDecimal;

public record UserDTO(
        String firstName,
        String lastName,
        String cpf,
        BigDecimal balance,
        String email,
        String type,
        String password
) {}
