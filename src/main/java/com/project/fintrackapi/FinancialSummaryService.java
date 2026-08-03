package com.project.fintrackapi;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Month;

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
        incomeService.getAllIncomes()
    }
}
