package com.project.fintrackapi.service;

import com.project.fintrackapi.dto.CreateBudgetRequest;
import com.project.fintrackapi.dto.UpdateBudgetRequest;
import com.project.fintrackapi.entity.Account;
import com.project.fintrackapi.entity.Budget;
import com.project.fintrackapi.entity.Expense;
import com.project.fintrackapi.exception.ResourceNotFoundException;
import com.project.fintrackapi.repository.AccountRepository;
import com.project.fintrackapi.repository.BudgetRepository;
import com.project.fintrackapi.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
public class BudgetService {
    private final AccountRepository accountRepository;
    private final BudgetRepository budgetRepository;
    private final ExpenseRepository expenseRepository;

    public BudgetService(AccountRepository accountRepository, BudgetRepository budgetRepository, ExpenseRepository expenseRepository) {
        this.accountRepository = accountRepository;
        this.budgetRepository = budgetRepository;
        this.expenseRepository = expenseRepository;
    }

    public List<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    public Budget getBudgetById(Long id) {
        return budgetRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Budget with ID " + id + " not found"));
    }

    public List<Budget> getBudgetsByMonth(Long accountId, YearMonth yearMonth) {
        return budgetRepository.findByAccountId(accountId).stream()
                .filter(budget -> budget.getBudgetMonth().equals(yearMonth))
                .toList();
    }

    public BigDecimal getMonthlyBudget(Long accountId, YearMonth yearMonth) {
        return budgetRepository.findByAccountId(accountId).stream()
                .filter(budget -> budget.getBudgetMonth().equals(yearMonth))
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

    public void addBudget(CreateBudgetRequest request) {
        Account account = accountRepository.findById(request.accountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account with ID " + request.accountId() + " not found"));

        Budget budget = new Budget(request.budgetCategory(), request.budgetTotal(), request.budgetMonth());
        budget.setAccount(account);

        budgetRepository.save(budget);
    }

    public void updateBudget(Long id, UpdateBudgetRequest request) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Budget with ID " + id + " not found"));

        budget.setBudgetCategory(request.budgetCategory());
        budget.setBudgetTotal(request.budgetTotal());
        budget.setBudgetMonth(request.budgetMonth());

        budgetRepository.save(budget);
    }

    public void deleteBudget(Long id) {
        if (!budgetRepository.existsById(id)) {
            throw new ResourceNotFoundException("Budget with ID " + id + " not found");
        }
        budgetRepository.deleteById(id);
    }
}
