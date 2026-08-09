package com.project.fintrackapi.controller;

import com.project.fintrackapi.enums.Category;
import com.project.fintrackapi.service.ExpenseService;
import com.project.fintrackapi.entity.Expense;
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
    public Expense getAExpenseById(@PathVariable Long id) {
        return expenseService.getExpenseById(id);
    }

    @GetMapping("total-spent/{id}/{yearMonth}")
    public BigDecimal getTotalSpentThisMonth(@PathVariable Long id, @PathVariable YearMonth yearMonth) {
        return expenseService.getTotalSpentThisMonth(id, yearMonth);
    }

    @GetMapping("/largest-expense/{id}/{yearMonth}")
    public Expense getLargestExpense(@PathVariable Long id, @PathVariable YearMonth yearMonth) {
        return expenseService.getLargestExpense(id, yearMonth);
    }

    @GetMapping("/largest-spending-category/{id}/{yearMonth}")
    public Category getLargestSpendingCategory(@PathVariable Long id, @PathVariable YearMonth yearMonth) {
        return expenseService.getLargestSpendingCategory(id, yearMonth);
    }

    @PostMapping
    public void addExpense(@RequestBody Expense expense) {
        expenseService.addExpense(expense);
    }

    @PutMapping("{id}")
    public void updateExpense(@PathVariable Long id, @RequestBody Expense expense) {
        expenseService.updateExpense(id, expense);
    }

    @DeleteMapping("{id}")
    public void deleteAExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
    }
}
