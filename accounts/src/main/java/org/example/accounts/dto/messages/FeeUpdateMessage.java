package org.example.accounts.dto.messages;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class FeeUpdateMessage implements Serializable {
    public enum UpdateAction {
        UPDATE_FEE
    }

    private UpdateAction action = UpdateAction.UPDATE_FEE;
}
