package org.example.accounts.service;

import io.swagger.client.model.Currency;
import org.example.accounts.client.ConverterClient;
import org.example.accounts.domain.AccountsRepository;
import org.example.accounts.domain.TransactionRepository;
import org.example.accounts.domain.entity.AccountEntity;
import org.example.accounts.domain.entity.TransactionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    private TransactionService service;

    @Mock
    private TransactionRepository repository;

    @Mock
    private AccountsRepository accountsRepository;

    @Mock
    private NotificationService notificationService;

    @Mock
    private ConverterClient converterClient;

    @BeforeEach
    void setUp() {
        when(accountsRepository.saveAll(any())).thenReturn(List.of());
        var toReturn = new TransactionEntity();
        toReturn.setAmount(BigDecimal.ZERO);
        when(repository.save(any())).thenReturn(toReturn);
        service = new TransactionService(repository, accountsRepository, notificationService, converterClient);
    }

    @Test
    void givenTransferWithoutFee_whenTransfer_thenOk() {
        AccountEntity sender = new AccountEntity();
        sender.setBalance(BigDecimal.valueOf(100));
        sender.setCurrency(Currency.RUB);
        AccountEntity receiver = new AccountEntity();
        receiver.setBalance(BigDecimal.valueOf(0));
        receiver.setCurrency(Currency.RUB);

        BigDecimal amount = BigDecimal.valueOf(100);
        BigDecimal fee = BigDecimal.ZERO;

        service.makeTransfer(sender, receiver, amount, fee);

        assertEquals(BigDecimal.ZERO.setScale(2), sender.getBalance());
        assertEquals(BigDecimal.valueOf(100).setScale(2), receiver.getBalance());
    }

    @Test
    void givenTransferWithFee_whenTransfer_thenOk() {
        AccountEntity sender = new AccountEntity();
        sender.setBalance(BigDecimal.valueOf(100));
        sender.setCurrency(Currency.RUB);
        AccountEntity receiver = new AccountEntity();
        receiver.setBalance(BigDecimal.valueOf(0));
        receiver.setCurrency(Currency.RUB);

        BigDecimal amount = BigDecimal.valueOf(100);
        BigDecimal fee = BigDecimal.valueOf(0.25);

        service.makeTransfer(sender, receiver, amount, fee);

        assertEquals(BigDecimal.ZERO.setScale(2), sender.getBalance());
        assertEquals(BigDecimal.valueOf(75).setScale(2), receiver.getBalance());
    }
}