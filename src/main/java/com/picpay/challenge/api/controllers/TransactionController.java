package com.picpay.challenge.api.controllers;

import com.picpay.challenge.domain.models.Transaction;
import com.picpay.challenge.domain.services.TransactionService;
import com.picpay.challenge.dtos.TransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(final TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody final TransactionDTO transactionDTO) {
        final Transaction transaction = this.transactionService.createTransaction(transactionDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

}
