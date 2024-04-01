package org.example.accounts.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.accounts.client.NotificationServiceClient;
import org.example.accounts.domain.OutboxCustomerUpdateRepository;
import org.example.accounts.domain.entity.CustomerEntity;
import org.example.accounts.domain.entity.OutboxCustomerUpdateEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageRelayService {
    private final NotificationServiceClient client;
    private final OutboxCustomerUpdateRepository updateRepository;
    @Value("${scheduler.batch_size}")
    private final int batchSize;

    public void persist(CustomerEntity customer, String message) {
        updateRepository.save(new OutboxCustomerUpdateEntity()
                .setMessage(message)
                .setCustomer(customer));
    }

    @Scheduled(cron = "${scheduler.cron}")
    @Async
    @Transactional
    public void outboxPublish() {
        log.info("outboxing");
        List<OutboxCustomerUpdateEntity> entities = updateRepository.fetchMessages(batchSize);
        entities.forEach(e -> {
            log.info("sending to customer %d".formatted(e.getCustomer().getId()));
            client.sendNotification(e);
            updateRepository.delete(e);
        });
    }
}
