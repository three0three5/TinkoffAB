package org.example.app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.app.service.RandomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/random")
@RequiredArgsConstructor
@Slf4j
public class RandomNumController {
    private final RandomService randomService;

    @GetMapping
    public ResponseEntity<Integer> getFixedNumber() {
        log.info("get request");
        int result = randomService.getFixedNumber();
        return ResponseEntity.ok(result);
    }
}
