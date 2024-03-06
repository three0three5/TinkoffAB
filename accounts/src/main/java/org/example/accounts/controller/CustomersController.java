package org.example.accounts.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import io.swagger.client.model.Currency;
import org.example.accounts.dto.request.CreateCustomerDto;
import org.example.accounts.dto.response.BalanceResponse;
import org.example.accounts.dto.response.CustomerIdResponse;
import org.example.accounts.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
@Slf4j
public class CustomersController {
    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<CustomerIdResponse> createNewCustomer(
            @Valid @RequestBody CreateCustomerDto createCustomerDto
    ) {
        log.info(createCustomerDto.toString());
        return ResponseEntity.ok(customerService.createCustomer(createCustomerDto));
    }

    @GetMapping("/{customerId}/balance")
    public ResponseEntity<BalanceResponse> getAllCustomerBalance(
            @PathVariable("customerId") Integer customerId,
            @RequestParam("currency") Currency currency
    ) {
        log.info(customerId.toString());
        return ResponseEntity.ok(customerService.getFullBalance(customerId, currency));
    }
}
