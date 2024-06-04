package org.example.accounts.service;

import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Observed(name = "WebSocketNotificationService")
@Slf4j
public class WebSocketNotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final String DEFAULT_DESTINATION = "/topic/accounts";

    public void sendMessageToClient(Object message) {
        log.info("Sending message through websocket");
        messagingTemplate.convertAndSend(DEFAULT_DESTINATION, message);
    }
}
