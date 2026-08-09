package com.project.fintrackapi.controller;

import com.project.fintrackapi.service.IncomeService;
import com.project.fintrackapi.entity.Income;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("api/v1/income")
public class IncomeController {
    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @GetMapping
    public List<Income> getAllIncomes() {
        return incomeService.getAllIncomes();
    }

    @GetMapping("{id}")
    public Income getIncomeById(@PathVariable Long id) {
        return incomeService.getIncomeById(id);
    }

    @GetMapping("total-income/{id}/{yearMonth}")
    public BigDecimal getTotalIncomeThisMonth(@PathVariable Long id, @PathVariable YearMonth yearMonth) {
        return incomeService.getTotalIncomeThisMonth(id, yearMonth);
    }

    @PostMapping
    public void addIncome(@RequestBody Income income) {
        incomeService.addIncome(income);
    }

    @PutMapping("{id}")
    public void updateIncome(@PathVariable Long id, @RequestBody Income income) {
        incomeService.updateIncome(id, income);
    }

    @DeleteMapping("{id}")
    public void deleteIncome(@PathVariable Long id) {
        incomeService.deleteIncome(id);
    }
}
