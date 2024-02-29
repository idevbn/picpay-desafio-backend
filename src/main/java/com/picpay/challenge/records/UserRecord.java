package com.picpay.challenge.records;

import java.math.BigDecimal;

public record UserRecord(
        String firstName,
        String lastName,
        String cpf,
        BigDecimal balance,
        String email,
        String type,
        String password
) {}
