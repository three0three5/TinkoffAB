package org.example.accounts.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AccountResponse {
    private int accountNumber;
}
