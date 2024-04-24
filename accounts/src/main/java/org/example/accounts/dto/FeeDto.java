package org.example.accounts.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeeDto {
    @DecimalMin(value = "0.0")
    @DecimalMax(value = "1.0")
    private BigDecimal fee;
}
