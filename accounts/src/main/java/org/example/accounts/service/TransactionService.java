package org.example.accounts.service;

import io.swagger.client.model.Currency;
import lombok.RequiredArgsConstructor;
import org.example.accounts.client.ConverterClient;
import org.example.accounts.domain.AccountsRepository;
import org.example.accounts.domain.TransactionRepository;
import org.example.accounts.domain.entity.AccountEntity;
import org.example.accounts.domain.entity.TransactionEntity;
import org.example.accounts.dto.request.AmountRequest;
import org.example.accounts.dto.response.TransactionResponse;
import org.example.accounts.exception.CustomerAccountNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository repository;
    private final AccountsRepository accountsRepository;
    private final NotificationService notificationService;
    private final ConverterClient converterClient;

    public List<TransactionResponse> getTransactionsForAccount(int accountNumber) {
        return repository.findAllByAccount_AccountNumberOrderByCreatedAtDesc(accountNumber)
                .stream().map(this::transactionEntityToTransactionResponse).toList();
    }

    @Transactional
    public TransactionResponse topUpAccount(int accountNumber, AmountRequest amountRequest) {
        AccountEntity accountEntity = accountsRepository
                .findById(accountNumber).orElseThrow(CustomerAccountNotFoundException::new);
        accountEntity.setBalance(accountEntity
                .getBalance()
                .add(amountRequest.getAmount())
                .setScale(2, RoundingMode.HALF_EVEN)
        );
        accountsRepository.save(accountEntity);
        return transactionEntityToTransactionResponse(
                persistedTransaction(accountEntity, amountRequest.getAmount(), true));
    }

    @Transactional
    public TransactionResponse makeTransfer(AccountEntity sender, AccountEntity receiver, BigDecimal amount) {
        Currency base = sender.getCurrency();
        BigDecimal newSenderBalance = sender.getBalance()
                .subtract(amount)
                .setScale(2, RoundingMode.HALF_EVEN);
        sender.setBalance(newSenderBalance);
        BigDecimal toAdd = getAmountToAdd(sender, receiver, amount, base);
        BigDecimal newReceiverBalance = receiver.getBalance()
                .add(toAdd)
                .setScale(2, RoundingMode.HALF_EVEN);
        receiver.setBalance(newReceiverBalance);

        accountsRepository.saveAll(List.of(sender, receiver));
        TransactionEntity toReturn = persistedTransaction(sender, amount.negate(), false);
        persistedTransaction(receiver, toAdd.setScale(2, RoundingMode.HALF_EVEN), false);
        return transactionEntityToTransactionResponse(toReturn);
    }

    private BigDecimal getAmountToAdd(AccountEntity sender, AccountEntity receiver, BigDecimal amount, Currency base) {
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
        return toAdd;
    }

    private TransactionEntity persistedTransaction(AccountEntity e, BigDecimal amount, boolean websocketOnly) {
        TransactionEntity toSave = new TransactionEntity();
        toSave.setAccount(e);
        toSave.setAmount(amount);
        if (websocketOnly) {
            notificationService.sendUpdateWebsocket(e);
        } else {
            notificationService.sendUpdate(e, amount);
        }
        return repository.save(toSave);
    }

    private TransactionResponse transactionEntityToTransactionResponse(TransactionEntity e) {
        return new TransactionResponse()
                .setTransactionId(e.getTransactionId())
                .setAmount(e.getAmount());
    }
}
