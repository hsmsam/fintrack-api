package com.project.fintrackapi;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BudgetService {
    BudgetRepository budgetRepository;

    public BudgetService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    public Budget getBudgetById(Long id) {
        return budgetRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("ID not found"));
    }

    public void addBudget(Budget budget) {
        budgetRepository.save(budget);
    }

    public void updateBudget(Long id, Budget budget) {
        Budget budgetExist = budgetRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("ID not found"));

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

    public BigDecimal totalIncomeThisMonth() {

    }

    public BigDecimal totalSpentThisMonth() {

    }

    public BigDecimal savingsThisMonth() {

    }

    public BigDecimal largestExpense() {

    }

    public Category largestSpendingCategory() {

    }

    public List<Budget> overSpentBudgets() {
        return getBudget();
    }

    public List<Budget> getBudget() {
        return getAllBudgets();
    }
}
