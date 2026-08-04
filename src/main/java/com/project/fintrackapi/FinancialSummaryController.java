package com.project.fintrackapi;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Month;

@RestController
@RequestMapping("api/v1/financial-summary")
public class FinancialSummaryController {
    FinancialSummaryService financialSummaryService;

    public FinancialSummaryController(FinancialSummaryService financialSummaryService) {
        this.financialSummaryService = financialSummaryService;
    }

    @GetMapping("/monthly-savings/{month}")
    public BigDecimal getMonthlySavings(@PathVariable Month month) {
        return financialSummaryService.monthlySavings(month);
    }

    @GetMapping("/running-balance")
    public BigDecimal getRunningBalance() {
        return financialSummaryService.runningBalance();
    }

    @GetMapping("/savings-rate/{month}")
    public BigDecimal getSavingsRate(@PathVariable Month month) {
        return financialSummaryService.savingsRate(month);
    }

    @GetMapping("/{month}")
    public FinancialSummary getFinancialSummary(@PathVariable Month month) {
        return financialSummaryService.getFinancialSummary(month);
    }
}
