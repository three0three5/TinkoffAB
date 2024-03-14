package org.example.accounts.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketNotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final String DEFAULT_DESTINATION = "/topic/accounts";

    public void sendMessageToClient(Object message) {
        messagingTemplate.convertAndSend(DEFAULT_DESTINATION, message);
    }
}
