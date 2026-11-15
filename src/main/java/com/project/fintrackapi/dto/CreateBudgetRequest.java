package com.project.fintrackapi.dto;

import com.project.fintrackapi.enums.Category;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.YearMonth;

public record CreateBudgetRequest(
        @NotNull(message = "Budget must have and belong to an account")
        Long accountId,
        @NotNull(message = "Budget must have a category")
        Category budgetCategory,
        @Positive(message = "Budget total must be greater than zero")
        @NotNull(message = "Budget total must have a value")
        BigDecimal budgetTotal,
        @NotNull(message = "Budget must have a month")
        YearMonth budgetMonth
) {
}
