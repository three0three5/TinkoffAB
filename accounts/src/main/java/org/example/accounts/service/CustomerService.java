package org.example.accounts.service;

import io.swagger.client.model.Currency;
import lombok.RequiredArgsConstructor;
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
public class CustomerService {
    private final CustomersRepository customersRepository;
    private final ConverterClient converterClient;

    public CustomerIdResponse createCustomer(CreateCustomerDto createCustomerDto) {
        CustomerEntity newCustomer = new CustomerEntity();
        newCustomer.setFirstName(createCustomerDto.getFirstName());
        newCustomer.setLastName(createCustomerDto.getLastName());
        newCustomer.setBirthDay(createCustomerDto.getBirthDay());
        newCustomer = customersRepository.save(newCustomer);
        return new CustomerIdResponse().setCustomerId(newCustomer.getId());
    }

    @Transactional
    public BalanceResponse getFullBalance(Integer customerId, Currency currency) {
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
        return new BalanceResponse()
                .setCurrency(currency)
                .setBalance(balance);
    }
}
