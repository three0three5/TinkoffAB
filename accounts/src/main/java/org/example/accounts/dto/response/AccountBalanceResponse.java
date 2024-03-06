package org.example.accounts.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;
import io.swagger.client.model.Currency;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class AccountBalanceResponse {
    private BigDecimal amount;
    private Currency currency;
}
