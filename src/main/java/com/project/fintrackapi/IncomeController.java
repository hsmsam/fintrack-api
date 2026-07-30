package com.project.fintrackapi;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/income")
public class IncomeController {
    IncomeService incomeService;

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

    @PostMapping
    public void addAIncome(@RequestBody Income income) {
        incomeService.addAIncome(income);
    }

    @PutMapping("{id}")
    public void updateAIncome(@PathVariable Long id, @RequestBody Income income) {
        incomeService.updateAIncome(id, income);
    }

    @DeleteMapping("{id}")
    public void deleteAIncome(@PathVariable Long id) {
        incomeService.deleteAIncome(id);
    }
}
