package com.project.fintrackapi.service;

import com.project.fintrackapi.enums.Category;
import com.project.fintrackapi.entity.Expense;
import com.project.fintrackapi.repository.ExpenseRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    public ExpenseService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }

    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("ID not found"));
    }

    public List<Expense> getExpenseByAccountId(Long id) {
        return expenseRepository.findByAccountId(id);
    }

    public BigDecimal getTotalSpentThisMonth(Long accountId, YearMonth yearMonth) {
        return expenseRepository.findByAccountId(accountId).stream()
                .filter(expense -> YearMonth.from(expense.getDatePaid()).equals(yearMonth))
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Expense getLargestExpense(Long accountId, YearMonth yearMonth) {
        return expenseRepository.findByAccountId(accountId).stream()
                .filter(Expense -> YearMonth.from(Expense.getDatePaid()).equals(yearMonth))
                .max(Comparator.comparing(Expense::getAmount))
                .orElse(null);
    }

    public Category getLargestSpendingCategory(Long accountId, YearMonth yearMonth) {
        return expenseRepository.findByAccountId(accountId).stream()
                .filter(expense -> YearMonth.from(expense.getDatePaid()).equals(yearMonth))
                .collect(Collectors.groupingBy(Expense::getCategory, Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add
                        )
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    public void addExpense(Expense expense) {
        expenseRepository.save(expense);
    }

    public void updateExpense(Long id, Expense expense) {
        Expense expenseExist = expenseRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("ID not found"));

        if (expense.getAmount() != null) {
            expenseExist.setAmount(expense.getAmount());
        }

        if (expense.getCategory() != null) {
            expenseExist.setCategory(expense.getCategory());
        }

        if (expense.getDatePaid() != null) {
            expenseExist.setDatePaid(expense.getDatePaid());
        }

        if (expense.getFrequency() != null) {
            expenseExist.setFrequency(expense.getFrequency());
        }

        if (expense.getDescription() != null) {
            expenseExist.setDescription(expense.getDescription());
        }

        expenseRepository.save(expenseExist);
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }
}
