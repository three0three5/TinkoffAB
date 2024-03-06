package org.example.accounts.domain.entity;

import io.swagger.client.model.Currency;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Digits;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Table(name = "accounts", uniqueConstraints = {@UniqueConstraint(columnNames = {"currency", "owner"})})
@NoArgsConstructor
public class AccountEntity {
    @Id
    @GeneratedValue
    private int accountNumber;

    @Digits(integer = 10, fraction = 2)
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private Currency currency;

    @JoinColumn(name = "owner", referencedColumnName="id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private CustomerEntity owner;
}
