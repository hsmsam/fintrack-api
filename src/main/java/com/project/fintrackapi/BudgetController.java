package com.project.fintrackapi;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/budget")
public class BudgetController {
    BudgetService budgetService;

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
