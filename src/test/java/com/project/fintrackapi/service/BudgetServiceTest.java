package com.project.fintrackapi.service;

import com.project.fintrackapi.entity.Budget;
import com.project.fintrackapi.entity.Expense;
import com.project.fintrackapi.enums.Category;
import com.project.fintrackapi.enums.Frequency;
import com.project.fintrackapi.repository.BudgetRepository;
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
class BudgetServiceTest {
    @Mock
    private ExpenseRepository expenseRepository;
    @Mock
    private BudgetRepository budgetRepository;

    @InjectMocks
    private BudgetService budgetService;

    @Test
    void getAllBudgets() {
        Budget firstBudget = new Budget(Category.LEISURE, BigDecimal.valueOf(100), YearMonth.of(2026, 6));
        Budget secondBudget = new Budget(Category.SHOPPING, BigDecimal.valueOf(200), YearMonth.of(2026, 6));
        Budget thirdBudget = new Budget(Category.SUBSCRIPTIONS, BigDecimal.valueOf(100), YearMonth.of(2026, 6));

        when(budgetRepository.findAll())
                .thenReturn(List.of(firstBudget, secondBudget, thirdBudget));

        List<Budget> allBudgets = budgetService.getAllBudgets();

        assertEquals(3, allBudgets.size());
    }

    @Test
    void getBudgetsByMonth() {
        Long accountId = 1L;
        Budget firstBudget = new Budget(Category.LEISURE, BigDecimal.valueOf(100), YearMonth.of(2026, 4));
        Budget secondBudget = new Budget(Category.SHOPPING, BigDecimal.valueOf(200), YearMonth.of(2026, 6));
        Budget thirdBudget = new Budget(Category.SUBSCRIPTIONS, BigDecimal.valueOf(100), YearMonth.of(2026, 6));

        when(budgetRepository.findByAccountId(accountId))
                .thenReturn(List.of(firstBudget, secondBudget, thirdBudget));

        List<Budget> result = budgetService.getBudgetsByMonth(accountId, YearMonth.of(2026, 4));

        assertEquals(1, result.size());
    }

    @Test
    void getMonthlyBudget() {
        Long accountId = 1L;
        Budget firstBudget = new Budget(Category.LEISURE, BigDecimal.valueOf(100), YearMonth.of(2026, 4));
        Budget secondBudget = new Budget(Category.SHOPPING, BigDecimal.valueOf(200), YearMonth.of(2026, 6));
        Budget thirdBudget = new Budget(Category.SUBSCRIPTIONS, BigDecimal.valueOf(100), YearMonth.of(2026, 6));

        when(budgetRepository.findByAccountId(accountId))
                .thenReturn(List.of(firstBudget, secondBudget, thirdBudget));

        BigDecimal monthlyBudget = budgetService.getMonthlyBudget(accountId, YearMonth.of(2026, 6));

        assertEquals(BigDecimal.valueOf(300), monthlyBudget);
    }


    @Test
    void getOverspentBudgets() {
        Long accountId = 1L;
        Budget firstBudget = new Budget(Category.SUBSCRIPTIONS, BigDecimal.valueOf(100), YearMonth.of(2026, 3));
        Budget secondBudget = new Budget(Category.SHOPPING, BigDecimal.valueOf(200), YearMonth.of(2026, 3));
        Budget thirdBudget = new Budget(Category.TRANSPORT, BigDecimal.valueOf(100), YearMonth.of(2026, 3));
        Expense firstExpense = new Expense(BigDecimal.valueOf(200), Category.SUBSCRIPTIONS, LocalDate.of(2026, 3, 7), Frequency.MONTHLY, "Phone contract, Netflix and other subscriptions");
        Expense secondExpense = new Expense(BigDecimal.valueOf(400), Category.SHOPPING, LocalDate.of(2026, 3, 1), Frequency.MONTHLY, "Buying clothes and shoes");
        Expense thirdExpense = new Expense(BigDecimal.valueOf(50), Category.TRANSPORT, LocalDate.of(2026, 3, 1), Frequency.MONTHLY, "Water, gas and electricity bills");
        Expense fourthExpense = new Expense(BigDecimal.valueOf(300), Category.FOOD, LocalDate.of(2026, 3, 15), Frequency.MONTHLY, "Monthly food shop");

        when(budgetRepository.findByAccountId(accountId))
                .thenReturn(List.of(firstBudget, secondBudget, thirdBudget));

        when(expenseRepository.findByAccountId(accountId))
                .thenReturn(List.of(firstExpense, secondExpense, thirdExpense, fourthExpense));

        List<Budget> overspentBudgets = budgetService.getOverspentBudgets(accountId, YearMonth.of(2026, 3));

        assertEquals(List.of(firstBudget, secondBudget), overspentBudgets);
    }
}