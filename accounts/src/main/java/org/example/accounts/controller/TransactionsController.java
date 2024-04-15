package org.example.accounts.controller;

import lombok.RequiredArgsConstructor;
import org.example.accounts.dto.response.TransactionResponse;
import org.example.accounts.service.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts/{accountNumber}/transactions")
public class TransactionsController {
    private final TransactionService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionResponse> getTransactions(@PathVariable int accountNumber) {
        return service.getTransactionsForAccount(accountNumber);
    }
}
