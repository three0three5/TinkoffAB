package org.example.accounts.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class TransactionEntity {
    @Id
    private UUID transactionId = UUID.randomUUID();
    private BigDecimal amount;
    @ManyToOne(fetch = FetchType.LAZY)
    private AccountEntity account;
    @CreationTimestamp
    private LocalDateTime createdAt;
}
