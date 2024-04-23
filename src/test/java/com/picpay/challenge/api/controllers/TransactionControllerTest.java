package com.picpay.challenge.api.controllers;

import com.picpay.challenge.api.exceptionhandler.ApiError;
import com.picpay.challenge.domain.models.Transaction;
import com.picpay.challenge.infrastructure.IntegrationTestInitializer;
import com.picpay.challenge.records.TransactionRecord;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TransactionControllerTest extends IntegrationTestInitializer {

    @Test
    @Sql(scripts = {"/database/clear_database.sql", "/database/transaction/create_transaction.sql"})
    public void shouldBeAbleToCompleteATransaction() {
        final String resourceLocation = "/transactions";

        final TransactionRecord transactionRecord = new TransactionRecord(
                BigDecimal.valueOf(200).setScale(2),
                2L,
                1L
        );

        final HttpEntity<TransactionRecord> httpEntity = new HttpEntity<>(transactionRecord);

        final ResponseEntity<Transaction> response = this.testRestTemplate.exchange(
                resourceLocation,
                HttpMethod.POST,
                httpEntity,
                Transaction.class
        );

        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);

        assertNotNull(response.getBody());

        final Transaction responseBody = response.getBody();

        assertEquals(1L, responseBody.getId());
        assertEquals(transactionRecord.receiverId(), responseBody.getReceiver().getId());
        assertEquals(transactionRecord.senderId(), responseBody.getSender().getId());
        assertEquals(transactionRecord.amount(), responseBody.getAmount());
    }

    @Test
    @Sql(scripts = {"/database/clear_database.sql", "/database/transaction/create_transaction_not_allowed.sql"})
    public void shouldNotBeAbleToCompleteATransactionWithUserSenderTypeShopkeeper() {
        final String resourceLocation = "/transactions";

        final TransactionRecord transactionRecord = new TransactionRecord(
                BigDecimal.valueOf(200).setScale(2),
                2L,
                1L
        );

        final HttpEntity<TransactionRecord> httpEntity = new HttpEntity<>(transactionRecord);

        final ResponseEntity<ApiError> response = this.testRestTemplate.exchange(
                resourceLocation,
                HttpMethod.POST,
                httpEntity,
                ApiError.class
        );

        assertNotNull(response);
        assertNotNull(response.getBody());

        final ApiError responseBody = response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseBody.getStatus());
        assertEquals("O lojista não está autorizado a realizar a transação.", responseBody.getResume());
    }

    @Test
    @Sql(scripts = {"/database/clear_database.sql", "/database/transaction/create_transaction_insufficient_amount.sql"})
    public void shouldNotBeAbleToCompleteATransactionWithUserSenderInsufficientAmount() {
        final String resourceLocation = "/transactions";

        final TransactionRecord transactionRecord = new TransactionRecord(
                BigDecimal.valueOf(200).setScale(2),
                2L,
                1L
        );

        final HttpEntity<TransactionRecord> httpEntity = new HttpEntity<>(transactionRecord);

        final ResponseEntity<ApiError> response = this.testRestTemplate.exchange(
                resourceLocation,
                HttpMethod.POST,
                httpEntity,
                ApiError.class
        );

        assertNotNull(response);
        assertNotNull(response.getBody());

        final ApiError responseBody = response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, responseBody.getStatus());
        assertEquals("Saldo insuficiente.", responseBody.getResume());
    }

    @Test
    @Sql(scripts = "/database/clear_database.sql")
    public void shouldNotBeAbleToCompleteATransactionWithUserNotFound() {
        final String resourceLocation = "/transactions";

        final TransactionRecord transactionRecord = new TransactionRecord(
                BigDecimal.valueOf(200).setScale(2),
                2L,
                1L
        );

        final HttpEntity<TransactionRecord> httpEntity = new HttpEntity<>(transactionRecord);

        final ResponseEntity<ApiError> response = this.testRestTemplate.exchange(
                resourceLocation,
                HttpMethod.POST,
                httpEntity,
                ApiError.class
        );

        assertNotNull(response);
        assertNotNull(response.getBody());

        final ApiError responseBody = response.getBody();

        assertEquals(HttpStatus.NOT_FOUND, responseBody.getStatus());
        assertEquals("Não existe um usuário cadastrado com o id=2", responseBody.getResume());
    }

}
