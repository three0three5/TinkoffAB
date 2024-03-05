package org.example.app.controller;

import io.swagger.client.model.Currency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.app.dto.CurrencyResponseDto;
import org.example.app.service.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/convert")
@RequiredArgsConstructor
@Slf4j
public class CurrencyController {
    private final CurrencyService currencyService;

    @GetMapping
    public ResponseEntity<CurrencyResponseDto> getConvertedCurrency(
            Authentication authentication,
            @RequestParam Currency from,
            @RequestParam Currency to,
            @RequestParam BigDecimal amount
    ) {
        log.info(from + " " + to + " " + amount);

        return ResponseEntity.ok(currencyService.convert(from, to, amount));
    }
}
