package com.project.fintrackapi.service;

import com.project.fintrackapi.dto.CreateExpenseRequest;
import com.project.fintrackapi.dto.UpdateExpenseRequest;
import com.project.fintrackapi.entity.Account;
import com.project.fintrackapi.enums.Category;
import com.project.fintrackapi.entity.Expense;
import com.project.fintrackapi.exception.ResourceNotFoundException;
import com.project.fintrackapi.repository.AccountRepository;
import com.project.fintrackapi.repository.ExpenseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final AccountRepository accountRepository;

    public ExpenseService(ExpenseRepository expenseRepository, AccountRepository accountRepository) {
        this.expenseRepository = expenseRepository;
        this.accountRepository = accountRepository;
    }

    public Page<Expense> getAllExpenses(Pageable page) {
        return expenseRepository.findAll(page);
    }

    public Expense getExpenseById(Long id) {
        return expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense with ID " + id + " not found"));
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
                .filter(expense -> YearMonth.from(expense.getDatePaid()).equals(yearMonth))
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

    public void addExpense(CreateExpenseRequest request) {
        Account account = accountRepository.findById(request.accountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account with ID " + request.accountId() + " not found"));

        Expense expense = new Expense(request.amount(), request.category(), request.datePaid(), request.frequency(), request.description());
        expense.setAccount(account);

        expenseRepository.save(expense);
    }

    public void updateExpense(Long id, UpdateExpenseRequest request) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense with ID " + id + " not found"));

        expense.setAmount(request.amount());
        expense.setCategory(request.category());
        expense.setDatePaid(request.datePaid());
        expense.setFrequency(request.frequency());
        expense.setDescription(request.description());

        expenseRepository.save(expense);
    }

    public void deleteExpense(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Expense with ID " + id + " not found");
        }
        expenseRepository.deleteById(id);
    }
}
