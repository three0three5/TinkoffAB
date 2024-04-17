package org.example.accounts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableCaching
@EnableAspectJAutoProxy
@EnableAsync
public class AccountsApp {
    public static void main(String[] args) {
        SpringApplication.run(AccountsApp.class, args);
    }
}