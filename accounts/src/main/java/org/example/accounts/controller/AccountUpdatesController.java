package org.example.accounts.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.accounts.dto.messages.AccountUpdateMessage;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class AccountUpdatesController {
    private final SimpMessagingTemplate messagingTemplate;

    public void sendUpdate(AccountUpdateMessage message) {
        log.info(message.toString());
        messagingTemplate.convertAndSend("/topic/accounts", message);
        log.info("sent message");
    }
}
