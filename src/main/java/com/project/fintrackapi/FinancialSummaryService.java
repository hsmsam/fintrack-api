package com.project.fintrackapi;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Month;
import java.util.List;

@Service
public class FinancialSummaryService {
    IncomeService incomeService;
    ExpenseService expenseService;
    BudgetService budgetService;

    public FinancialSummaryService(IncomeService incomeService, ExpenseService expenseService, BudgetService budgetService) {
        this.incomeService = incomeService;
        this.expenseService = expenseService;
        this.budgetService = budgetService;
    }

    public BigDecimal monthlySavings(Month month) {
        BigDecimal income = incomeService.totalIncomeThisMonth(month);
        BigDecimal expense = expenseService.totalSpentThisMonth(month);
        return income.subtract(expense);
    }

    public BigDecimal runningBalance() {
        BigDecimal totalIncome = incomeService.getAllIncomes().stream()
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = expenseService.getAllExpenses().stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalIncome.subtract(totalExpenses);
    }

    public BigDecimal savingsRate(Month month) {
        BigDecimal totalIncome = incomeService.totalIncomeThisMonth(month);

        if (totalIncome.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return monthlySavings(month)
                .divide(totalIncome, 2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    public FinancialSummary getFinancialSummary(Month month) {
        List<Budget> budgets = budgetService.getBudgetsByMonth(month);
        BigDecimal monthlyIncome = incomeService.totalIncomeThisMonth(month);
        BigDecimal monthlySpending = expenseService.totalSpentThisMonth(month);
        BigDecimal monthlyBudget = budgetService.monthlyBudget(month);
        BigDecimal monthlySaving = monthlySavings(month);
        BigDecimal savingsRate = savingsRate(month);
        BigDecimal runningBalance = runningBalance();
        BigDecimal largestExpense = expenseService.largestExpense(month).getAmount();
        Category largestSpendingCategory = expenseService.largestSpendingCategory(month);

        return new FinancialSummary(budgets, monthlyIncome, monthlySpending, monthlyBudget, monthlySaving, savingsRate, runningBalance, largestExpense, largestSpendingCategory);
    }
}
