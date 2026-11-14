package com.project.fintrackapi.controller;

import com.project.fintrackapi.dto.CreateIncomeRequest;
import com.project.fintrackapi.dto.UpdateIncomeRequest;
import com.project.fintrackapi.service.IncomeService;
import com.project.fintrackapi.entity.Income;
import jakarta.validation.Valid;
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

    @GetMapping("total-income/{accountId}/{yearMonth}")
    public BigDecimal getTotalIncomeThisMonth(@PathVariable Long accountId, @PathVariable YearMonth yearMonth) {
        return incomeService.getTotalIncomeThisMonth(accountId, yearMonth);
    }

    @PostMapping
    public void addIncome(@Valid @RequestBody CreateIncomeRequest request) {
        incomeService.addIncome(request);
    }

    @PutMapping("{id}")
    public void updateIncome(@PathVariable Long id, @Valid @RequestBody UpdateIncomeRequest request) {
        incomeService.updateIncome(id, request);
    }

    @DeleteMapping("{id}")
    public void deleteIncome(@PathVariable Long id) {
        incomeService.deleteIncome(id);
    }
}
