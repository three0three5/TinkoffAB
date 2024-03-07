package org.example.accounts.service;

import io.swagger.client.model.Currency;
import lombok.RequiredArgsConstructor;
import org.example.accounts.client.ConverterClient;
import org.example.accounts.domain.AccountsRepository;
import org.example.accounts.domain.CustomersRepository;
import org.example.accounts.domain.entity.AccountEntity;
import org.example.accounts.domain.entity.CustomerEntity;
import org.example.accounts.dto.request.AmountRequest;
import org.example.accounts.dto.request.CreateAccountDto;
import org.example.accounts.dto.request.TransferRequest;
import org.example.accounts.dto.response.AccountBalanceResponse;
import org.example.accounts.dto.response.AccountResponse;
import org.example.accounts.exception.CustomerAccountNotFoundException;
import org.example.accounts.exception.CustomerNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import static org.example.accounts.utils.Constants.AMOUNT_NOT_VALID;
import static org.example.accounts.utils.Constants.NOT_ENOUGH_MONEY;

@Service
@RequiredArgsConstructor
public class AccountsService {
    private final AccountsRepository accountsRepository;
    private final CustomersRepository customersRepository;
    private final ConverterClient converterClient;

    public AccountResponse createAccount(CreateAccountDto createAccountDto) {
        Optional<CustomerEntity> customer = customersRepository.findById(createAccountDto.getCustomerId());
        if (customer.isEmpty()) throw new CustomerNotFoundException();
        CustomerEntity customerEntity = customer.get();
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setCurrency(createAccountDto.getCurrency());
        accountEntity.setBalance(BigDecimal.ZERO);
        accountEntity.setOwner(customerEntity);
        accountEntity = accountsRepository.save(accountEntity);
        return new AccountResponse().setAccountNumber(accountEntity.getAccountNumber());
    }

    public AccountBalanceResponse getAccountBalance(Integer accountNumber) {
        AccountEntity entity = accountsRepository.findById(accountNumber)
                .orElseThrow(CustomerAccountNotFoundException::new);
        return new AccountBalanceResponse()
                .setCurrency(entity.getCurrency())
                .setAmount(entity.getBalance());
    }

    @Transactional
    public void topUpAccount(Integer accountNumber, AmountRequest amountRequest) {
        if (amountRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException(AMOUNT_NOT_VALID);
        AccountEntity accountEntity = accountsRepository
                .findById(accountNumber).orElseThrow(CustomerAccountNotFoundException::new);
        accountEntity.setBalance(accountEntity
                .getBalance()
                .add(amountRequest.getAmount())
                .setScale(2, RoundingMode.HALF_EVEN)
        );
        accountsRepository.save(accountEntity);
    }

    @Transactional
    public void transfer(TransferRequest transferRequest) {
        BigDecimal amount = transferRequest.getAmountInSenderCurrency().setScale(2, RoundingMode.HALF_EVEN);
        if (amount.compareTo(BigDecimal.ZERO) == 0) return;
        if (amount.compareTo(BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("Amount not valid");

        AccountEntity sender = accountsRepository
                .findById(transferRequest.getSenderAccount())
                .orElseThrow(CustomerAccountNotFoundException::new);
        checkBalance(transferRequest, sender);
        AccountEntity receiver = accountsRepository
                .findById(transferRequest.getReceiverAccount())
                .orElseThrow(CustomerAccountNotFoundException::new);

        makeTransfer(sender, receiver, amount);
        accountsRepository.saveAll(List.of(sender, receiver));
    }

    private static void checkBalance(TransferRequest transferRequest, AccountEntity sender) {
        if (sender.getBalance()
                .subtract(transferRequest.getAmountInSenderCurrency())
                .compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(NOT_ENOUGH_MONEY);
        }
    }

    private void makeTransfer(AccountEntity sender, AccountEntity receiver, BigDecimal amount) {
        Currency base = sender.getCurrency();
        BigDecimal newSenderBalance = sender.getBalance()
                .subtract(amount)
                .setScale(2, RoundingMode.HALF_EVEN);
        sender.setBalance(newSenderBalance);
        BigDecimal toAdd;
        if (base.equals(receiver.getCurrency())) {
            toAdd = amount;
        } else {
            toAdd = converterClient.convertCurrency(
                    sender.getCurrency(),
                    receiver.getCurrency(),
                    amount
            ).amount();
        }
        BigDecimal newReceiverBalance = receiver.getBalance()
                .add(toAdd)
                .setScale(2, RoundingMode.HALF_EVEN);
        receiver.setBalance(newReceiverBalance);
    }
}
