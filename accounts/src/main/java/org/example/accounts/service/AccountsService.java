package org.example.accounts.service;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.accounts.domain.AccountsRepository;
import org.example.accounts.domain.CustomersRepository;
import org.example.accounts.domain.entity.AccountEntity;
import org.example.accounts.domain.entity.CustomerEntity;
import org.example.accounts.dto.request.AmountRequest;
import org.example.accounts.dto.request.CreateAccountDto;
import org.example.accounts.dto.request.TransferRequest;
import org.example.accounts.dto.response.AccountBalanceResponse;
import org.example.accounts.dto.response.AccountResponse;
import org.example.accounts.dto.response.TransactionResponse;
import org.example.accounts.exception.CustomerAccountNotFoundException;
import org.example.accounts.exception.CustomerNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.example.accounts.utils.Constants.AMOUNT_NOT_VALID;
import static org.example.accounts.utils.Constants.NOT_ENOUGH_MONEY;

@Service
@RequiredArgsConstructor
@Observed(name = "AccountsService")
@Slf4j
public class AccountsService {
    private final AccountsRepository accountsRepository;
    private final CustomersRepository customersRepository;
    private final NotificationService notificationService;
    private final TransactionService transactionService;
    private final FeeService feeService;

    public AccountResponse createAccount(CreateAccountDto createAccountDto) {
        Optional<CustomerEntity> customer = customersRepository.findById(createAccountDto.getCustomerId());
        if (customer.isEmpty()) throw new CustomerNotFoundException();
        CustomerEntity customerEntity = customer.get();
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setCurrency(createAccountDto.getCurrency());
        accountEntity.setBalance(BigDecimal.ZERO);
        accountEntity.setOwner(customerEntity);
        accountEntity = accountsRepository.save(accountEntity);
        notificationService.sendUpdateWebsocket(accountEntity);
        return new AccountResponse().setAccountNumber(accountEntity.getAccountNumber());
    }

    public AccountBalanceResponse getAccountBalance(Integer accountNumber) {
        log.info("get balance for {}", accountNumber);
        AccountEntity entity = accountsRepository.findById(accountNumber)
                .orElseThrow(CustomerAccountNotFoundException::new);
        return new AccountBalanceResponse()
                .setCurrency(entity.getCurrency())
                .setAmount(entity.getBalance());
    }

    public TransactionResponse topUpAccount(Integer accountNumber, AmountRequest amountRequest) {
        if (amountRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException(AMOUNT_NOT_VALID);
        return transactionService.topUpAccount(accountNumber, amountRequest);
    }

    @Transactional
    public TransactionResponse transfer(TransferRequest transferRequest) {
        log.info("Transfer from {} to {}", transferRequest.getSenderAccount(), transferRequest.getReceiverAccount());
        BigDecimal amount = transferRequest.getAmountInSenderCurrency().setScale(2, RoundingMode.HALF_EVEN);
        if (amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount not valid");

        BigDecimal fee = feeService.getFee();
        log.info("fee equals: {}", fee);
        AccountEntity sender = accountsRepository
                .findById(transferRequest.getSenderAccount())
                .orElseThrow(CustomerAccountNotFoundException::new);
        checkBalance(transferRequest, sender);
        AccountEntity receiver = accountsRepository
                .findById(transferRequest.getReceiverAccount())
                .orElseThrow(CustomerAccountNotFoundException::new);

        return transactionService.makeTransfer(sender, receiver, amount, fee);
    }

    private static void checkBalance(TransferRequest transferRequest, AccountEntity sender) {
        BigDecimal toSub = transferRequest.getAmountInSenderCurrency();
        if (sender.getBalance()
                .subtract(toSub)
                .compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(NOT_ENOUGH_MONEY);
        }
    }
}
