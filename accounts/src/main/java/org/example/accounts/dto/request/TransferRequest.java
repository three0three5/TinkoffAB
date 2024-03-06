package org.example.accounts.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferRequest {
    private int receiverAccount;
    private int senderAccount;
    @NotNull
    private BigDecimal amountInSenderCurrency;
}
