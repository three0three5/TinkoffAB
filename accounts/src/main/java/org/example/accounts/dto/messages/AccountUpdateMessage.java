package org.example.accounts.dto.messages;

import io.swagger.client.model.Currency;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AccountUpdateMessage {
    private int accountNumber;
    private Currency currency;
    private String balance;
}
