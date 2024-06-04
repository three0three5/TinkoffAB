package org.example.accounts.service;

import io.micrometer.observation.annotation.Observed;
import io.swagger.client.model.Currency;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.accounts.client.ConverterClient;
import org.example.accounts.domain.CustomersRepository;
import org.example.accounts.domain.entity.CustomerEntity;
import org.example.accounts.dto.request.CreateCustomerDto;
import org.example.accounts.dto.response.BalanceResponse;
import org.example.accounts.dto.response.CustomerIdResponse;
import org.example.accounts.exception.CustomerNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
@Observed(name = "CustomerService")
@Slf4j
public class CustomerService {
    private final CustomersRepository customersRepository;
    private final ConverterClient converterClient;

    public CustomerIdResponse createCustomer(CreateCustomerDto createCustomerDto) {
        log.info("creating new customer with name {} {}",
                createCustomerDto.getFirstName(), createCustomerDto.getLastName());
        CustomerEntity newCustomer = new CustomerEntity();
        newCustomer.setFirstName(createCustomerDto.getFirstName());
        newCustomer.setLastName(createCustomerDto.getLastName());
        newCustomer.setBirthDay(createCustomerDto.getBirthDay());
        newCustomer = customersRepository.save(newCustomer);
        return new CustomerIdResponse().setCustomerId(newCustomer.getId());
    }

    @Transactional
    public BalanceResponse getFullBalance(Integer customerId, Currency currency) {
        log.info("get full balance for {} in currency {}", customerId, currency);
        BigDecimal balance = customersRepository.findById(customerId)
                .orElseThrow(CustomerNotFoundException::new)
                .getAccounts().stream()
                .map(accountEntity -> {
                    if (accountEntity.getCurrency().equals(currency)) {
                        return accountEntity.getBalance();
                    }
                    return converterClient.convertCurrency(
                            accountEntity.getCurrency(),
                            currency,
                            accountEntity.getBalance()
                    ).amount();
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_EVEN);
        log.info("mapped with result: {}", balance);
        return new BalanceResponse()
                .setCurrency(currency)
                .setBalance(balance);
    }
}
