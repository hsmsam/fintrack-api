package com.project.fintrackapi.dto;

import com.project.fintrackapi.enums.Category;
import com.project.fintrackapi.enums.Frequency;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateExpenseRequest(
        @Positive(message = "Expense amount must be greater than zero")
        @NotNull(message = "Expense must have a value")
        BigDecimal amount,
        @NotNull(message = "Expense must have a category")
        Category category,
        @NotNull(message = "Expense must have a date for when it's paid")
        LocalDate datePaid,
        @NotNull(message = "Expense must include a frequency")
        Frequency frequency,
        String description
) {
}
