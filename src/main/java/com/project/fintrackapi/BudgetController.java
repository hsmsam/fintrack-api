package com.project.fintrackapi;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Month;
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

    @GetMapping("{id}/{month}")
    public List<Budget> getBudgetsByMonth(@PathVariable Long id, @PathVariable Month month) {
        return budgetService.getBudgetsByMonth(id, month);
    }

    @GetMapping("/monthly-budget/{id}/{month}")
    public BigDecimal getMonthlyBudget(@PathVariable Long id, @PathVariable Month month) {
        return budgetService.getMonthlyBudget(id, month);
    }

    @GetMapping("/overspent-budgets/{id}/{month}")
    public List<Budget> getOverspentBudgets(@PathVariable Long id, @PathVariable Month month) {
        return budgetService.getOverspentBudgets(id, month);
    }

    @PostMapping
    public void addBudget(@RequestBody Budget budget) {
        budgetService.addBudget(budget);
    }

    @PutMapping("{id}")
    public void updateBudget(@PathVariable Long id, @RequestBody Budget budget) {
        budgetService.updateBudget(id, budget);
    }

    @DeleteMapping("{id}")
    public void deleteBudget(@PathVariable Long id) {
        budgetService.deleteBudget(id);
    }
}
