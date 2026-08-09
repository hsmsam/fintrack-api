package com.project.fintrackapi.service;

import com.project.fintrackapi.entity.Income;
import com.project.fintrackapi.enums.Frequency;
import com.project.fintrackapi.repository.IncomeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IncomeServiceTest {
    @Mock
    private IncomeRepository incomeRepository;

    @InjectMocks
    private IncomeService incomeService;

    @Test
    void getAllIncomes() {
        Income firstIncome = new Income(BigDecimal.valueOf(2000), "Work", LocalDate.of(2026, 4, 3), Frequency.MONTHLY, "Monthly work wage");
        Income secondIncome = new Income(BigDecimal.valueOf(150), "Side income", LocalDate.of(2026, 4, 3), Frequency.WEEKLY, "Forex trading");

        when(incomeRepository.findAll())
                .thenReturn(List.of(firstIncome, secondIncome));

        List<Income> allIncomes = incomeService.getAllIncomes();

        assertEquals(2, allIncomes.size());
    }

    @Test
    void getTotalIncomeThisMonth() {
        Long accountId = 1L;
        Income firstIncome = new Income(BigDecimal.valueOf(2000), "Work", LocalDate.of(2026, 4, 3), Frequency.MONTHLY, "Monthly work wage");
        Income secondIncome = new Income(BigDecimal.valueOf(600), "Side income", LocalDate.of(2026, 4, 27), Frequency.MONTHLY, "Forex trading");

        when(incomeRepository.findByAccountId(accountId))
                .thenReturn(List.of(firstIncome, secondIncome));

        BigDecimal result = incomeService.getTotalIncomeThisMonth(accountId, YearMonth.of(2026, 4));

        assertEquals(BigDecimal.valueOf(2600), result);
    }
}