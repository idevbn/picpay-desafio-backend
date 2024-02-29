package com.picpay.challenge.domain.services;

import com.picpay.challenge.domain.exceptions.TransactionNotAuthorizedException;
import com.picpay.challenge.domain.models.Transaction;
import com.picpay.challenge.domain.models.User;
import com.picpay.challenge.domain.repositories.TransactionRepository;
import com.picpay.challenge.records.TransactionRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {

    @Value("${picpay.api.url.transaction}")
    private String AUTHORIZATION_URL;

    private final UserService userService;
    private final TransactionRepository transactionRepository;
    private final RestTemplate restTemplate;


    @Autowired
    public TransactionService(final UserService userService,
                              final TransactionRepository transactionRepository,
                              final RestTemplate restTemplate) {
        this.userService = userService;
        this.transactionRepository = transactionRepository;
        this.restTemplate = restTemplate;
    }

    public Transaction createTransaction(final TransactionRecord transaction) {
        final User sender = this.userService.findUserById(transaction.senderId());
        final User receiver = this.userService.findUserById(transaction.receiverId());

        this.userService.validateTransaction(sender, transaction.amount());

        final boolean isAuthorized = this.authorizeTransaction(sender, transaction.amount());

        if (!isAuthorized) {
            throw new TransactionNotAuthorizedException();
        }

        final Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transaction.amount());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        // Atualizando saldo do usuário que realiza a transferência
        sender.setBalance(sender.getBalance().subtract(transaction.amount()));

        // Atualizando saldo do usuário que recebe a transferência
        receiver.setBalance(receiver.getBalance().add(transaction.amount()));

        this.transactionRepository.save(newTransaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);

        return newTransaction;
    }

    public boolean authorizeTransaction(final User sender, final BigDecimal amount) {
        final ResponseEntity<Map> authorizationResponse = this.restTemplate
                .getForEntity(this.AUTHORIZATION_URL, Map.class);


        return authorizationResponse.getStatusCode().equals(HttpStatus.OK) &&
                authorizationResponse.getBody().get("message").equals("Autorizado");
    }

}
