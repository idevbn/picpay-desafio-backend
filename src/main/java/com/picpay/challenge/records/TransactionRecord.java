package com.picpay.challenge.records;

import java.math.BigDecimal;

public record TransactionRecord(BigDecimal amount, Long senderId, Long receiverId) {
}
