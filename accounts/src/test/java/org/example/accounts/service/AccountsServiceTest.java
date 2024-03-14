package org.example.accounts.service;

import io.swagger.client.model.Currency;
import org.example.accounts.client.ConverterClient;
import org.example.accounts.controller.AccountUpdatesController;
import org.example.accounts.domain.AccountsRepository;
import org.example.accounts.domain.CustomersRepository;
import org.example.accounts.domain.entity.AccountEntity;
import org.example.accounts.dto.request.TransferRequest;
import org.example.accounts.dto.response.CurrencyResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountsServiceTest {
    @InjectMocks
    AccountsService accountsService;
    @Mock
    private AccountsRepository accountsRepository;
    @Mock
    private CustomersRepository customersRepository;
    @Mock
    private ConverterClient converterClient;
    @Mock
    private AccountUpdatesController accountUpdatesController;

    @BeforeEach
    void setUp() {
        doReturn(new CurrencyResponseDto(Currency.RUB, BigDecimal.valueOf(2000)))
                .when(converterClient)
                .convertCurrency(eq(Currency.EUR), eq(Currency.RUB), any());
    }

    @Test
    void givenTwoAccounts_whenTransfer_thenCorrectTransfer() {
        AccountEntity sender = new AccountEntity();
        sender.setAccountNumber(1);
        sender.setCurrency(Currency.EUR);
        sender.setBalance(BigDecimal.valueOf(1000));

        AccountEntity receiver = new AccountEntity();
        receiver.setAccountNumber(2);
        receiver.setCurrency(Currency.RUB);
        receiver.setBalance(BigDecimal.valueOf(5000));

        when(accountsRepository.findById(1)).thenReturn(Optional.of(sender));
        when(accountsRepository.findById(2)).thenReturn(Optional.of(receiver));

        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setSenderAccount(1);
        transferRequest.setReceiverAccount(2);
        transferRequest.setAmountInSenderCurrency(BigDecimal.valueOf(200));

        accountsService.transfer(transferRequest);

        assertEquals(BigDecimal.valueOf(800).setScale(2, RoundingMode.HALF_EVEN), sender.getBalance());

        assertEquals(BigDecimal.valueOf(7000).setScale(2, RoundingMode.HALF_EVEN), receiver.getBalance());
    }
}