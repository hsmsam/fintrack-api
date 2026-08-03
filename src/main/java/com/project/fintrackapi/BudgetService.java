package com.project.fintrackapi;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@Service
public class BudgetService {
    BudgetRepository budgetRepository;
    ExpenseRepository expenseRepository;

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

    public List<Budget> overSpentBudgets(Month month) {
        List<Budget> budgets = budgetRepository.findAll();
        List<Expense> expenses = expenseRepository.findAll();
        List<Budget> overspent = new ArrayList<>();

        BigDecimal totalSpent;

        for (Budget budget : budgets) {
            if (budget.getBudgetMonth() != month) {
                continue;
            }

            totalSpent = BigDecimal.ZERO;

            for (Expense expense : expenses) {
                if (expense.getDatePaid().getMonth() == month && expense.getCategory() == budget.getBudgetCategory()) {
                    totalSpent = totalSpent.add(expense.getAmount());
                }
            }

            if (totalSpent.compareTo(budget.getBudgetTarget()) > 0) {
                overspent.add(budget);
            }
        }

        return overspent;
    }
}
