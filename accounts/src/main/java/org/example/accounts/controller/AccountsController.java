package org.example.accounts.controller;

import com.example.idempotency.Idempotent;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.accounts.dto.request.AmountRequest;
import org.example.accounts.dto.request.CreateAccountDto;
import org.example.accounts.dto.response.AccountBalanceResponse;
import org.example.accounts.dto.response.AccountResponse;
import org.example.accounts.dto.response.TransactionResponse;
import org.example.accounts.service.AccountsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
@Slf4j
@RequiredArgsConstructor
public class AccountsController {
    private final AccountsService accountsService;

    @PostMapping
    public ResponseEntity<AccountResponse> createNewCustomerAccount(
            @Valid @RequestBody CreateAccountDto createAccountDto
    ) {
        log.info(createAccountDto.toString());
        return ResponseEntity.ok(accountsService.createAccount(createAccountDto));
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountBalanceResponse> getAccountBalance(@PathVariable Integer accountNumber) {
        log.info(accountNumber.toString());
        return ResponseEntity.ok(accountsService.getAccountBalance(accountNumber));
    }

    @Idempotent
    @PostMapping("/{accountNumber}/top-up")
    @ResponseStatus(HttpStatus.OK)
    public TransactionResponse topUpAccount(
            @PathVariable Integer accountNumber,
            @Valid @RequestBody AmountRequest amountRequest
    ) {
        log.info("top up with {} {}", accountNumber, amountRequest);
        return accountsService.topUpAccount(accountNumber, amountRequest);
    }
}
