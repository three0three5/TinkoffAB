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

import java.math.BigDecimal;

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
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new BalanceResponse()
                .setCurrency(currency)
                .setBalance(balance);
    }
}
