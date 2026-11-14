package com.project.fintrackapi.dto;

import com.project.fintrackapi.enums.Frequency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateIncomeRequest(
        @NotNull(message = "Income must belong to and have an account")
        Long accountId,
        @Positive(message = "Income amount must be greater than zero")
        @NotNull(message = "Income amount must have a value")
        BigDecimal amount,
        @NotBlank(message = "Income must include the name of the source of income")
        String source,
        @NotNull(message = "Income must include a date for when it's received")
        LocalDate dateReceived,
        @NotNull(message = "Income must include a frequency")
        Frequency frequency,
        String description
) {
}
