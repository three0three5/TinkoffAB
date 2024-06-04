package org.example.accounts.service;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.example.accounts.domain.entity.AccountEntity;
import org.example.accounts.domain.entity.CustomerEntity;
import org.example.accounts.dto.messages.AccountUpdateMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.example.accounts.utils.Constants.CUSTOMER_UPDATE_MESSAGE_TEMPLATE;

@Service
@RequiredArgsConstructor
@Observed(name = "NotificationService")
@Slf4j
public class NotificationService {
    private final WebSocketNotificationService webSocketService;
    private final MessageRelayService messageRelayService;

    @Transactional
    public void sendUpdate(AccountEntity accountEntity, BigDecimal diff) {
        log.info("sending update for account {}", accountEntity.getAccountNumber());
        sendUpdateOutbox(accountEntity, diff);
        sendUpdateWebsocket(accountEntity);
    }

    @Transactional
    public void sendUpdateOutbox(AccountEntity accountEntity, BigDecimal diff) {
        CustomerEntity owner = accountEntity.getOwner();
        String diffString = diff.toString();
        if (diff.compareTo(BigDecimal.ZERO) > 0) {
            diffString = "+" + diffString;
        }
        String message = CUSTOMER_UPDATE_MESSAGE_TEMPLATE.formatted(
                accountEntity.getAccountNumber(),
                diffString,
                accountEntity.getBalance());
        messageRelayService.persist(owner, message);
        messageRelayService.outboxPublish();
    }

    public void sendUpdateWebsocket(AccountEntity accountEntity) {
        webSocketService.sendMessageToClient(new AccountUpdateMessage()
                .setAccountNumber(accountEntity.getAccountNumber())
                .setBalance(accountEntity.getBalance().setScale(2, RoundingMode.HALF_EVEN))
                .setCurrency(accountEntity.getCurrency()));
    }
}
