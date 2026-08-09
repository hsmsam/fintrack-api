package com.project.fintrackapi.service;

import com.project.fintrackapi.enums.Category;
import com.project.fintrackapi.entity.FinancialSummary;
import com.project.fintrackapi.entity.Budget;
import com.project.fintrackapi.entity.Expense;
import com.project.fintrackapi.entity.Income;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.YearMonth;
import java.util.List;

@Service
public class FinancialSummaryService {
    private final IncomeService incomeService;
    private final ExpenseService expenseService;
    private final BudgetService budgetService;

    public FinancialSummaryService(IncomeService incomeService, ExpenseService expenseService, BudgetService budgetService) {
        this.incomeService = incomeService;
        this.expenseService = expenseService;
        this.budgetService = budgetService;
    }

    public BigDecimal getMonthlySavings(Long accountId, YearMonth yearMonth) {
        BigDecimal income = incomeService.getTotalIncomeThisMonth(accountId, yearMonth);
        BigDecimal expense = expenseService.getTotalSpentThisMonth(accountId, yearMonth);
        return income.subtract(expense);
    }

    public BigDecimal getRunningBalance(Long accountId) {
        BigDecimal totalIncome = incomeService.getIncomeByAccountId(accountId).stream()
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = expenseService.getExpenseByAccountId(accountId).stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalIncome.subtract(totalExpenses);
    }

    public BigDecimal getSavingsRate(Long accountId, YearMonth yearMonth) {
        BigDecimal totalIncome = incomeService.getTotalIncomeThisMonth(accountId, yearMonth);

        if (totalIncome.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return getMonthlySavings(accountId, yearMonth)
                .divide(totalIncome, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public FinancialSummary getFinancialSummary(Long accountId, YearMonth yearMonth) {
        List<Budget> budgets = budgetService.getBudgetsByMonth(accountId, yearMonth);
        BigDecimal monthlyIncome = incomeService.getTotalIncomeThisMonth(accountId, yearMonth);
        BigDecimal monthlySpending = expenseService.getTotalSpentThisMonth(accountId, yearMonth);
        BigDecimal monthlyBudget = budgetService.getMonthlyBudget(accountId, yearMonth);
        BigDecimal monthlySaving = getMonthlySavings(accountId, yearMonth);
        BigDecimal savingsRate = getSavingsRate(accountId, yearMonth);
        BigDecimal runningBalance = getRunningBalance(accountId);
        Expense largestExpenseEntity = expenseService.getLargestExpense(accountId, yearMonth);
        BigDecimal largestExpense = (largestExpenseEntity != null) ? largestExpenseEntity.getAmount() : BigDecimal.ZERO;
        Category largestSpendingCategory = expenseService.getLargestSpendingCategory(accountId, yearMonth);

        return new FinancialSummary(budgets, monthlyIncome, monthlySpending, monthlyBudget, monthlySaving, savingsRate, runningBalance, largestExpense, largestSpendingCategory);
    }
}
