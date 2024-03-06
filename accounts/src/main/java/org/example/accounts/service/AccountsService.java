package org.example.accounts.service;

import lombok.RequiredArgsConstructor;
import org.example.accounts.dto.request.AmountRequest;
import org.example.accounts.dto.request.CreateAccountDto;
import org.example.accounts.dto.request.TransferRequest;
import org.example.accounts.dto.response.AccountBalanceResponse;
import org.example.accounts.dto.response.AccountResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountsService {

    public AccountResponse createAccount(CreateAccountDto createAccountDto) {
        return null; // TODO
    }

    public AccountBalanceResponse getAccountBalance(String accountNumber) {
        return null; // TODO
    }

    public void topUpAccount(Integer accountNumber, AmountRequest amountRequest) {
        // TODO
    }

    public void transfer(TransferRequest transferRequest) {
        // TODO
    }
}
