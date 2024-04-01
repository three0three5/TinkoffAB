package org.example.accounts.client;

import lombok.RequiredArgsConstructor;
import org.example.accounts.domain.entity.OutboxCustomerUpdateEntity;
import org.example.accounts.dto.messages.CustomerUpdateMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class NotificationServiceClient {
    private final WebClient webClient;
    @Value("${notification-service.post-endpoint}")
    private final String url;

    public void sendNotification(OutboxCustomerUpdateEntity e) {
        CustomerUpdateMessage body = new CustomerUpdateMessage()
                .setMessage(e.getMessage())
                .setCustomerId(e.getCustomer().getId());
        webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Object.class)
                .block();
    }
}
