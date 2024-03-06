package org.example.accounts.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.accounts.dto.request.TransferRequest;
import org.example.accounts.service.AccountsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/transfers")
public class TransfersController {
    private final AccountsService accountsService;

    @PostMapping
    public ResponseEntity<?> transfer(@Valid @RequestBody TransferRequest transferRequest) {
        accountsService.transfer(transferRequest);
        return ResponseEntity.ok().build();
    }
}
