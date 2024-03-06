package org.example.accounts.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import io.swagger.client.model.Currency;

@Data
public class CreateAccountDto {
    private int customerId;
    @NotNull
    private Currency currency;
}
