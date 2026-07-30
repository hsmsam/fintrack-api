package com.project.fintrackapi;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/expense")
public class ExpenseController {
    ExpenseService expenseService;

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

    @PostMapping
    public void addAExpense(@RequestBody Expense expense) {
        expenseService.addAExpense(expense);
    }

    @PutMapping("{id}")
    public void updateAExpense(@PathVariable Long id, @RequestBody Expense expense) {
        expenseService.updateAExpense(id, expense);
    }

    @DeleteMapping("{id}")
    public void deleteAExpense(@PathVariable Long id) {
        expenseService.deleteAExpense(id);
    }
}
