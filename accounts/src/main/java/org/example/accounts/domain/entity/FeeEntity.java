package org.example.accounts.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.example.accounts.utils.Constants;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.example.accounts.utils.Constants.FEE_COLUMN_DEFINITION;

@Entity
@Getter
@Setter
@Table(name = "fee")
public class FeeEntity {
    @Id
    private String id = Constants.DEFAULT_FEE_ENTITY_ID;

    @Column(columnDefinition = FEE_COLUMN_DEFINITION, name = "value")
    private BigDecimal value;

    private LocalDateTime updatedAt;
}
