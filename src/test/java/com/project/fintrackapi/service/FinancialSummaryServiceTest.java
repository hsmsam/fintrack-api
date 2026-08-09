package com.project.fintrackapi.service;

import com.project.fintrackapi.entity.Budget;
import com.project.fintrackapi.entity.Expense;
import com.project.fintrackapi.entity.FinancialSummary;
import com.project.fintrackapi.entity.Income;
import com.project.fintrackapi.enums.Category;
import com.project.fintrackapi.enums.Frequency;
import com.project.fintrackapi.repository.BudgetRepository;
import com.project.fintrackapi.repository.ExpenseRepository;
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
class FinancialSummaryServiceTest {
    @Mock
    private IncomeService incomeService;
    @Mock
    private ExpenseService expenseService;
    @Mock
    private BudgetService budgetService;
    @InjectMocks
    private FinancialSummaryService financialSummaryService;

    @Test
    void getMonthlySavings() {
        Long accountId = 1L;

        when(incomeService.getTotalIncomeThisMonth(accountId, YearMonth.of(2026, 3)))
                .thenReturn(BigDecimal.valueOf(2150));

        when(expenseService.getTotalSpentThisMonth(accountId, YearMonth.of(2026, 3)))
                .thenReturn(BigDecimal.valueOf(580));

        BigDecimal result = financialSummaryService.getMonthlySavings(accountId, YearMonth.of(2026, 3));

        assertEquals(BigDecimal.valueOf(1570), result);
    }


    @Test
    void getRunningBalance() {
        Long accountId = 1L;

        Income firstIncome = new Income(BigDecimal.valueOf(2000), "Work", LocalDate.of(2026, 3, 1), Frequency.MONTHLY, "Monthly work income");
        Income secondIncome = new Income(BigDecimal.valueOf(400), "Side income", LocalDate.of(2026, 3, 28), Frequency.MONTHLY, "Forex trading");
        Expense firstExpense = new Expense(BigDecimal.valueOf(800), Category.HOUSING, LocalDate.of(2026, 3, 3), Frequency.MONTHLY, "Monthly house rent");
        Expense secondExpense = new Expense(BigDecimal.valueOf(150), Category.LEISURE, LocalDate.of(2026, 3, 28), Frequency.MONTHLY, "Going out doing activities and getting food");

        when(incomeService.getIncomeByAccountId(accountId))
                .thenReturn(List.of(firstIncome, secondIncome));

        when(expenseService.getExpenseByAccountId(accountId))
                .thenReturn(List.of(firstExpense, secondExpense));

        BigDecimal result = financialSummaryService.getRunningBalance(accountId);

        assertEquals(BigDecimal.valueOf(1450), result);
    }


    @Test
    void getSavingsRate() {
        Long accountId = 1L;

        when(incomeService.getTotalIncomeThisMonth(accountId, YearMonth.of(2026, 3)))
                .thenReturn(BigDecimal.valueOf(2400));

        when(expenseService.getTotalSpentThisMonth(accountId, YearMonth.of(2026, 3)))
                .thenReturn(BigDecimal.valueOf(800));

        BigDecimal result = financialSummaryService.getSavingsRate(accountId, YearMonth.of(2026, 3));

        assertEquals(0, BigDecimal.valueOf(67.00).compareTo(result));
    }


    @Test
    void getFinancialSummary() {
        Long accountId = 1L;
        Income firstIncome = new Income(BigDecimal.valueOf(2000), "Work", LocalDate.of(2026, 3, 1), Frequency.MONTHLY, "Monthly work income");
        Income secondIncome = new Income(BigDecimal.valueOf(400), "Side income", LocalDate.of(2026, 3, 28), Frequency.MONTHLY, "Forex trading");
        Expense firstExpense = new Expense(BigDecimal.valueOf(800), Category.HOUSING, LocalDate.of(2026, 3, 3), Frequency.MONTHLY, "Monthly house rent");
        Expense secondExpense = new Expense(BigDecimal.valueOf(200), Category.LEISURE, LocalDate.of(2026, 3, 28), Frequency.MONTHLY, "Going out doing activities and getting food");
        Budget firstBudget = new Budget(Category.LEISURE, BigDecimal.valueOf(150), YearMonth.of(2026, 3));

        when(incomeService.getIncomeByAccountId(accountId))
                .thenReturn(List.of(firstIncome, secondIncome));

        when(expenseService.getExpenseByAccountId(accountId))
                .thenReturn(List.of(firstExpense, secondExpense));

        when(budgetService.getBudgetsByMonth(accountId, YearMonth.of(2026, 3)))
                .thenReturn(List.of(firstBudget));

        when(incomeService.getTotalIncomeThisMonth(accountId, YearMonth.of(2026, 3)))
                .thenReturn(BigDecimal.valueOf(2400));

        when(expenseService.getTotalSpentThisMonth(accountId, YearMonth.of(2026, 3)))
                .thenReturn(BigDecimal.valueOf(1000));

        when(budgetService.getMonthlyBudget(accountId, YearMonth.of(2026, 3)))
                .thenReturn(BigDecimal.valueOf(150));

        when(expenseService.getLargestExpense(accountId, YearMonth.of(2026, 3)))
                .thenReturn(firstExpense);

        when(expenseService.getLargestSpendingCategory(accountId, YearMonth.of(2026, 3)))
                .thenReturn(Category.HOUSING);

        FinancialSummary result = financialSummaryService.getFinancialSummary(accountId, YearMonth.of(2026, 3));

        assertEquals(1, result.getBudget().size());
        assertEquals(0, BigDecimal.valueOf(2400).compareTo(result.getMonthlyIncome()));
        assertEquals(0, BigDecimal.valueOf(1000).compareTo(result.getMonthlySpending()));
        assertEquals(0, BigDecimal.valueOf(150).compareTo(result.getMonthlyBudget()));
        assertEquals(0, BigDecimal.valueOf(1400).compareTo(result.getMonthlySaving()));
        assertEquals(0, BigDecimal.valueOf(58.00).compareTo(result.getSavingsRate()));
        assertEquals(0, BigDecimal.valueOf(1400).compareTo(result.getRunningBalance()));
        assertEquals(0, BigDecimal.valueOf(800).compareTo(result.getLargestExpense()));
        assertEquals(Category.HOUSING, result.getLargestSpendingCategory());
    }
}