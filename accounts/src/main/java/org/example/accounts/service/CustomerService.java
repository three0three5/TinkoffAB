package org.example.accounts.service;

import lombok.RequiredArgsConstructor;
import org.example.accounts.dto.request.CreateCustomerDto;
import org.example.accounts.dto.response.BalanceResponse;
import org.example.accounts.dto.response.CustomerIdResponse;
import org.springframework.stereotype.Service;
import io.swagger.client.model.Currency;

@Service
@RequiredArgsConstructor
public class CustomerService {

    public CustomerIdResponse createCustomer(CreateCustomerDto createCustomerDto) {
        return null; // TODO
    }

    public BalanceResponse getFullBalance(Integer customerId, Currency currency) {
        return null; // TODO
    }
}
