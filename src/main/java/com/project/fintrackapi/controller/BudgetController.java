package com.project.fintrackapi.controller;

import com.project.fintrackapi.service.BudgetService;
import com.project.fintrackapi.entity.Budget;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("api/v1/budget")
public class BudgetController {
    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @GetMapping
    public List<Budget> getAllBudgets() {
        return budgetService.getAllBudgets();
    }

    @GetMapping("{id}")
    public Budget getBudgetById(@PathVariable Long id) {
        return budgetService.getBudgetById(id);
    }

    @GetMapping("{accountId}/{yearMonth}")
    public List<Budget> getBudgetsByMonth(@PathVariable Long accountId, @PathVariable YearMonth yearMonth) {
        return budgetService.getBudgetsByMonth(accountId, yearMonth);
    }

    @GetMapping("/monthly-budget/{accountId}/{yearMonth}")
    public BigDecimal getMonthlyBudget(@PathVariable Long accountId, @PathVariable YearMonth yearMonth) {
        return budgetService.getMonthlyBudget(accountId, yearMonth);
    }

    @GetMapping("/overspent-budgets/{accountId}/{yearMonth}")
    public List<Budget> getOverspentBudgets(@PathVariable Long accountId, @PathVariable YearMonth yearMonth) {
        return budgetService.getOverspentBudgets(accountId, yearMonth);
    }

    @PostMapping
    public void addBudget(@Valid @RequestBody Budget budget) {
        budgetService.addBudget(budget);
    }

    @PutMapping("{id}")
    public void updateBudget(@PathVariable Long id, @Valid @RequestBody Budget budget) {
        budgetService.updateBudget(id, budget);
    }

    @DeleteMapping("{id}")
    public void deleteBudget(@PathVariable Long id) {
        budgetService.deleteBudget(id);
    }
}
