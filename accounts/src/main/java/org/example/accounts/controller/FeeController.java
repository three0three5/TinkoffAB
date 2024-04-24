package org.example.accounts.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.accounts.dto.FeeDto;
import org.example.accounts.service.FeeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class FeeController {
    private final FeeService service;

    @PostMapping("/configs")
    @ResponseStatus(HttpStatus.OK)
    public FeeDto updateFee(@RequestBody @Valid FeeDto feeDto) {
        return service.update(feeDto);
    }

    @GetMapping("/configs")
    public FeeDto getFee() {
        return new FeeDto(service.getFee());
    }
}
