package org.example.accounts.dto.messages;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CustomerUpdateMessage {
    private int customerId;
    private String message;
}
