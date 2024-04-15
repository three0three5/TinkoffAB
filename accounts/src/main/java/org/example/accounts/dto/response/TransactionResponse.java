package org.example.accounts.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class TransactionResponse {
    private UUID transactionId;
    private BigDecimal amount;
}
