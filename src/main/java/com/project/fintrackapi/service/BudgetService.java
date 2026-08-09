package com.project.fintrackapi.service;

import com.project.fintrackapi.entity.Budget;
import com.project.fintrackapi.entity.Expense;
import com.project.fintrackapi.repository.BudgetRepository;
import com.project.fintrackapi.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class BudgetService {
    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;

    public BudgetService(BudgetRepository budgetRepository, ExpenseRepository expenseRepository) {
        this.budgetRepository = budgetRepository;
        this.expenseRepository = expenseRepository;
    }

    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    public Budget getBudgetById(Long id) {
        return budgetRepository.findById(id).orElseThrow(() -> new IllegalStateException("ID not found"));
    }

    public List<Budget> getBudgetsByMonth(Long accountId, YearMonth yearMonth) {
        return budgetRepository.findByAccountId(accountId).stream()
                .filter(budget -> YearMonth.from(budget.getBudgetMonth()).equals(yearMonth))
                .toList();
    }

    public BigDecimal getMonthlyBudget(Long accountId, YearMonth yearMonth) {
        return budgetRepository.findByAccountId(accountId).stream()
                .filter(budget -> YearMonth.from(budget.getBudgetMonth()).equals(yearMonth))
                .map(Budget::getBudgetTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<Budget> getOverspentBudgets(Long accountId, YearMonth yearMonth) {
        List<Budget> budgets = budgetRepository.findByAccountId(accountId);
        List<Expense> expenses = expenseRepository.findByAccountId(accountId);
        List<Budget> overspent = new ArrayList<>();

        BigDecimal totalSpent;

        for (Budget budget : budgets) {
            if (!budget.getBudgetMonth().equals(yearMonth)) {
                continue;
            }

            totalSpent = BigDecimal.ZERO;

            for (Expense expense : expenses) {
                if (YearMonth.from(expense.getDatePaid()).equals(yearMonth) && expense.getCategory() == budget.getBudgetCategory()) {
                    totalSpent = totalSpent.add(expense.getAmount());
                }
            }

            if (totalSpent.compareTo(budget.getBudgetTotal()) > 0) {
                overspent.add(budget);
            }
        }
        return overspent;
    }

    public void addBudget(Budget budget) {
        budgetRepository.save(budget);
    }

    public void updateBudget(Long id, Budget budget) {
        Budget budgetExist = budgetRepository.findById(id).orElseThrow(() -> new IllegalStateException("ID not found"));

        if (budget.getBudgetCategory() != null) {
            budgetExist.setBudgetCategory(budget.getBudgetCategory());
        }

        if (budget.getBudgetTotal() != null) {
            budgetExist.setBudgetTotal(budget.getBudgetTotal());
        }

        if (budget.getBudgetMonth() != null) {
            budgetExist.setBudgetMonth(budget.getBudgetMonth());
        }

        budgetRepository.save(budgetExist);
    }

    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }
}
