package com.project.fintrackapi;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Month;
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

    @GetMapping("total-spent/{id}/{month}")
    public BigDecimal getTotalSpentThisMonth(@PathVariable Long id, @PathVariable Month month) {
        return expenseService.getTotalSpentThisMonth(id, month);
    }

    @GetMapping("/largest-expense/{id}/{month}")
    public Expense getLargestExpense(@PathVariable Long id, @PathVariable Month month) {
        return expenseService.getLargestExpense(id, month);
    }

    @GetMapping("/largest-spending-category/{id}/{month}")
    public Category getlargestSpendingCategory(@PathVariable Long id, @PathVariable Month month) {
        return expenseService.getlargestSpendingCategory(id, month);
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
