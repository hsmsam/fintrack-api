package com.project.fintrackapi.service;

import com.project.fintrackapi.entity.Expense;
import com.project.fintrackapi.enums.Category;
import com.project.fintrackapi.enums.Frequency;
import com.project.fintrackapi.repository.ExpenseRepository;
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
class ExpenseServiceTest {
    @Mock
    private ExpenseRepository expenseRepository;

    @InjectMocks
    private ExpenseService expenseService;

    @Test
    void getAllExpenses() {
        Expense firstExpense = new Expense(BigDecimal.valueOf(80), Category.SUBSCRIPTIONS, LocalDate.of(2026, 3, 7), Frequency.MONTHLY, "Phone contract, Netflix and other subscriptions");
        Expense secondExpense = new Expense(BigDecimal.valueOf(500), Category.HOUSING, LocalDate.of(2026, 3, 1), Frequency.MONTHLY, "House rent");
        Expense thirdExpense = new Expense(BigDecimal.valueOf(200), Category.BILLS, LocalDate.of(2026, 3, 1), Frequency.MONTHLY, "Water, gas and electricity bills");
        Expense fourthExpense = new Expense(BigDecimal.valueOf(300), Category.FOOD, LocalDate.of(2026, 3, 15), Frequency.MONTHLY, "Monthly food shop");

        when(expenseRepository.findAll())
                .thenReturn(List.of(firstExpense, secondExpense, thirdExpense, fourthExpense));

        List<Expense> allExpenses = expenseService.getAllExpenses();

        assertEquals(4, allExpenses.size());
    }

    @Test
    void getTotalSpentThisMonth() {
        Long accountId = 1L;
        Expense firstExpense = new Expense(BigDecimal.valueOf(80), Category.SUBSCRIPTIONS, LocalDate.of(2026, 3, 7), Frequency.MONTHLY, "Phone contract, Netflix and other subscriptions");
        Expense secondExpense = new Expense(BigDecimal.valueOf(500), Category.HOUSING, LocalDate.of(2026, 3, 1), Frequency.MONTHLY, "House rent");
        Expense thirdExpense = new Expense(BigDecimal.valueOf(200), Category.BILLS, LocalDate.of(2026, 3, 1), Frequency.MONTHLY, "Water, gas and electricity bills");
        Expense fourthExpense = new Expense(BigDecimal.valueOf(300), Category.FOOD, LocalDate.of(2026, 3, 15), Frequency.MONTHLY, "Monthly food shop");

        when(expenseRepository.findByAccountId(accountId))
                .thenReturn(List.of(firstExpense, secondExpense, thirdExpense, fourthExpense));

        BigDecimal result = expenseService.getTotalSpentThisMonth(accountId, YearMonth.of(2026, 3));

        assertEquals(BigDecimal.valueOf(1080), result);
    }

    @Test
    void getLargestExpense_returnsHighestAmountExpense() {
        Long accountId = 1L;
        Expense firstExpense = new Expense(BigDecimal.valueOf(80), Category.SUBSCRIPTIONS, LocalDate.of(2026, 3, 7), Frequency.MONTHLY, "Phone contract, Netflix and other subscriptions");
        Expense secondExpense = new Expense(BigDecimal.valueOf(500), Category.HOUSING, LocalDate.of(2026, 3, 1), Frequency.MONTHLY, "House rent");
        Expense thirdExpense = new Expense(BigDecimal.valueOf(200), Category.BILLS, LocalDate.of(2026, 3, 1), Frequency.MONTHLY, "Water, gas and electricity bills");
        Expense fourthExpense = new Expense(BigDecimal.valueOf(300), Category.FOOD, LocalDate.of(2026, 3, 15), Frequency.MONTHLY, "Monthly food shop");

        when(expenseRepository.findByAccountId(accountId))
                .thenReturn(List.of(firstExpense, secondExpense, thirdExpense, fourthExpense));

        Expense result = expenseService.getLargestExpense(accountId, YearMonth.of(2026, 3));

        assertEquals(BigDecimal.valueOf(500), result.getAmount());
    }

    @Test
    void getLargestExpense_returnsNullWhenNoExpensesInMonth() {
        Long accountId = 1L;

        when(expenseRepository.findByAccountId(accountId))
                .thenReturn(List.of());

        Expense result = expenseService.getLargestExpense(accountId, YearMonth.of(2026, 3));

        assertNull(result);
    }

    @Test
    void getLargestSpendingCategory_returnsLargestSpendingCategory() {
        Long accountId = 1L;
        Expense firstExpense = new Expense(BigDecimal.valueOf(80), Category.SUBSCRIPTIONS, LocalDate.of(2026, 3, 7), Frequency.MONTHLY, "Phone contract, Netflix and other subscriptions");
        Expense secondExpense = new Expense(BigDecimal.valueOf(500), Category.HOUSING, LocalDate.of(2026, 3, 1), Frequency.MONTHLY, "House rent");
        Expense thirdExpense = new Expense(BigDecimal.valueOf(200), Category.BILLS, LocalDate.of(2026, 3, 1), Frequency.MONTHLY, "Water, gas and electricity bills");
        Expense fourthExpense = new Expense(BigDecimal.valueOf(300), Category.FOOD, LocalDate.of(2026, 3, 15), Frequency.MONTHLY, "Monthly food shop");

        when(expenseRepository.findByAccountId(accountId))
                .thenReturn(List.of(firstExpense, secondExpense, thirdExpense, fourthExpense));

        Category result = expenseService.getLargestSpendingCategory(accountId, YearMonth.of(2026, 3));

        assertEquals(secondExpense.getCategory(), result);
    }

    @Test
    void getLargestSpendingCategory_returnsNullWhenNoExpense() {
        Long accountId = 1L;

        when(expenseRepository.findByAccountId(accountId))
                .thenReturn(List.of());

        Category result = expenseService.getLargestSpendingCategory(accountId, YearMonth.of(2026, 3));

        assertNull(result);
    }
}