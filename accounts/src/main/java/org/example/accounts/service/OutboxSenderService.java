package org.example.accounts.service;

import lombok.RequiredArgsConstructor;
import org.example.accounts.client.NotificationServiceClient;
import org.example.accounts.domain.OutboxCustomerUpdateRepository;
import org.example.accounts.domain.entity.OutboxCustomerUpdateEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OutboxSenderService {
    private final NotificationServiceClient client;
    private final OutboxCustomerUpdateRepository updateRepository;

    @Transactional(propagation = Propagation.NESTED)
    public void sendWithDelete(OutboxCustomerUpdateEntity e) {
        client.sendNotification(e);
        updateRepository.delete(e);
    }
}
