package com.project.fintrackapi;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
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

    public BigDecimal getMonthlySavings(Long accountId, Month month) {
        BigDecimal income = incomeService.getTotalIncomeThisMonth(accountId, month);
        BigDecimal expense = expenseService.getTotalSpentThisMonth(accountId, month);
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

    public BigDecimal getSavingsRate(Long accountId, Month month) {
        BigDecimal totalIncome = incomeService.getTotalIncomeThisMonth(accountId, month);

        if (totalIncome.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return getMonthlySavings(accountId, month)
                .divide(totalIncome, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public FinancialSummary getFinancialSummary(Long accountId, Month month) {
        List<Budget> budgets = budgetService.getBudgetsByMonth(accountId, month);
        BigDecimal monthlyIncome = incomeService.getTotalIncomeThisMonth(accountId, month);
        BigDecimal monthlySpending = expenseService.getTotalSpentThisMonth(accountId, month);
        BigDecimal monthlyBudget = budgetService.getMonthlyBudget(accountId, month);
        BigDecimal monthlySaving = getMonthlySavings(accountId, month);
        BigDecimal savingsRate = getSavingsRate(accountId, month);
        BigDecimal runningBalance = getRunningBalance(accountId);
        Expense largestExpenseEntity = expenseService.getLargestExpense(accountId, month);
        BigDecimal largestExpense = (largestExpenseEntity != null) ? largestExpenseEntity.getAmount() : BigDecimal.ZERO;
        Category largestSpendingCategory = expenseService.getlargestSpendingCategory(accountId, month);

        return new FinancialSummary(budgets, monthlyIncome, monthlySpending, monthlyBudget, monthlySaving, savingsRate, runningBalance, largestExpense, largestSpendingCategory);
    }
}
