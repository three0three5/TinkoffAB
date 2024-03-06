package org.example.accounts.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.example.accounts.dto.validator.annotation.ValidBirthday;

import java.time.LocalDate;

@Data
@Accessors(chain = true)
public class CreateCustomerDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @ValidBirthday
    private LocalDate birthDay;
}
