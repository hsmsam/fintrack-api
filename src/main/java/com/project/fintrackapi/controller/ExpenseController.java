package com.project.fintrackapi.controller;

import com.project.fintrackapi.enums.Category;
import com.project.fintrackapi.service.ExpenseService;
import com.project.fintrackapi.entity.Expense;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("api/v1/expense")
public class ExpenseController {
    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    public List<Expense> getAllExpenses() {
        return expenseService.getAllExpenses();
    }

    @GetMapping("{id}")
    public Expense getExpenseById(@PathVariable Long id) {
        return expenseService.getExpenseById(id);
    }

    @GetMapping("total-spent/{accountId}/{yearMonth}")
    public BigDecimal getTotalSpentThisMonth(@PathVariable Long accountId, @PathVariable YearMonth yearMonth) {
        return expenseService.getTotalSpentThisMonth(accountId, yearMonth);
    }

    @GetMapping("/largest-expense/{accountId}/{yearMonth}")
    public Expense getLargestExpense(@PathVariable Long accountId, @PathVariable YearMonth yearMonth) {
        return expenseService.getLargestExpense(accountId, yearMonth);
    }

    @GetMapping("/largest-spending-category/{accountId}/{yearMonth}")
    public Category getLargestSpendingCategory(@PathVariable Long accountId, @PathVariable YearMonth yearMonth) {
        return expenseService.getLargestSpendingCategory(accountId, yearMonth);
    }

    @PostMapping
    public void addExpense(@Valid @RequestBody Expense expense) {
        expenseService.addExpense(expense);
    }

    @PutMapping("{id}")
    public void updateExpense(@PathVariable Long id, @Valid @RequestBody Expense expense) {
        expenseService.updateExpense(id, expense);
    }

    @DeleteMapping("{id}")
    public void deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
    }
}
