package com.project.fintrackapi;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.Month;

@RestController
@RequestMapping("api/v1/financial-summary")
public class FinancialSummaryController {
    private final FinancialSummaryService financialSummaryService;

    public FinancialSummaryController(FinancialSummaryService financialSummaryService) {
        this.financialSummaryService = financialSummaryService;
    }

    @GetMapping("/monthly-savings/{accountId}/{month}")
    public BigDecimal getMonthlySavings(@PathVariable Long accountId, @PathVariable Month month) {
        return financialSummaryService.getMonthlySavings(accountId, month);
    }

    @GetMapping("/running-balance/{accountId}")
    public BigDecimal getRunningBalance(@PathVariable Long accountId) {
        return financialSummaryService.getRunningBalance(accountId);
    }

    @GetMapping("/savings-rate/{accountId}/{month}")
    public BigDecimal getSavingsRate(@PathVariable Long accountId, @PathVariable Month month) {
        return financialSummaryService.getSavingsRate(accountId, month);
    }

    @GetMapping("/summary/{accountId}/{month}")
    public FinancialSummary getFinancialSummary(@PathVariable Long accountId, @PathVariable Month month) {
        return financialSummaryService.getFinancialSummary(accountId, month);
    }
}
