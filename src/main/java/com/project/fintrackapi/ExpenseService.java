package com.project.fintrackapi;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseService {
    ExpenseRepository expenseRepository;

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

    public void addAExpense(Expense expense) {
        expenseRepository.save(expense);
    }

    public void updateAExpense(Long id, Expense expense) {
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

    public void deleteAExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    public Expense largestExpense(Month month) {
        return getAllExpenses().stream()
                .filter(Expense -> Expense.getDatePaid().getMonth() == month)
                .max(Comparator.comparing(Expense::getAmount))
                .orElseThrow(null);
    }

    public BigDecimal totalSpentThisMonth(Month month) {
        return getAllExpenses().stream()
                .filter(expense -> expense.getDatePaid().getMonth() == month)
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Category largestSpendingCategory(Month month) {
        return getAllExpenses().stream()
                .filter(expense -> expense.getDatePaid().getMonth() == month)
                .collect(Collectors.groupingBy(Expense::getCategory, Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add
                        )
                ))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }
}
