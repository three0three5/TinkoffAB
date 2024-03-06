package org.example.accounts.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;
import io.swagger.client.model.Currency;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
public class BalanceResponse {
    private BigDecimal balance;
    private Currency currency;
}
