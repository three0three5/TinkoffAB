package org.example.accounts.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AmountRequest {
    @NotNull
    private BigDecimal amount;
}
