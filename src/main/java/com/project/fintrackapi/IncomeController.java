package com.project.fintrackapi;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Month;
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

    @GetMapping("total-income/{id}/{month}")
    public BigDecimal getTotalIncomeThisMonth(@PathVariable Long id, @PathVariable Month month) {
        return incomeService.getTotalIncomeThisMonth(id, month);
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
