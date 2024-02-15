package org.example.app.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomService {
    private static final int fixed = (new Random()).nextInt(1024);
    public int getFixedNumber() {
        return fixed;
    }
}
