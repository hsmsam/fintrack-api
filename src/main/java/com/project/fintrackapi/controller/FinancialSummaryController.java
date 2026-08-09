package com.project.fintrackapi.controller;

import com.project.fintrackapi.entity.FinancialSummary;
import com.project.fintrackapi.service.FinancialSummaryService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.YearMonth;

@RestController
@RequestMapping("api/v1/financial-summary")
public class FinancialSummaryController {
    private final FinancialSummaryService financialSummaryService;

    public FinancialSummaryController(FinancialSummaryService financialSummaryService) {
        this.financialSummaryService = financialSummaryService;
    }

    @GetMapping("/monthly-savings/{accountId}/{yearMonth}")
    public BigDecimal getMonthlySavings(@PathVariable Long accountId, @PathVariable YearMonth yearMonth) {
        return financialSummaryService.getMonthlySavings(accountId, yearMonth);
    }

    @GetMapping("/running-balance/{accountId}")
    public BigDecimal getRunningBalance(@PathVariable Long accountId) {
        return financialSummaryService.getRunningBalance(accountId);
    }

    @GetMapping("/savings-rate/{accountId}/{yearMonth}")
    public BigDecimal getSavingsRate(@PathVariable Long accountId, @PathVariable YearMonth yearMonth) {
        return financialSummaryService.getSavingsRate(accountId, yearMonth);
    }

    @GetMapping("/summary/{accountId}/{yearMonth}")
    public FinancialSummary getFinancialSummary(@PathVariable Long accountId, @PathVariable YearMonth yearMonth) {
        return financialSummaryService.getFinancialSummary(accountId, yearMonth);
    }
}
