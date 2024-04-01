package org.example.accounts.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Table(name = "outbox_updates")
public class OutboxCustomerUpdateEntity {
    @Id
    @GeneratedValue
    private long id;

    @JoinColumn(referencedColumnName="id")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private CustomerEntity customer;

    @Column
    private String message;

    @Column
    @CreationTimestamp
    private LocalDateTime createdAt;
}
