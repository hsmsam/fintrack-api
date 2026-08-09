package com.project.fintrackapi.entity;

import com.project.fintrackapi.enums.Category;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class FinancialSummary {
    List<Budget> budget;
    BigDecimal monthlyIncome;
    BigDecimal monthlySpending;
    BigDecimal monthlyBudget;
    BigDecimal monthlySaving;
    BigDecimal savingsRate;
    BigDecimal runningBalance;
    BigDecimal largestExpense;
    Category largestSpendingCategory;

    public FinancialSummary(List<Budget> budget, BigDecimal monthlyIncome, BigDecimal monthlySpending, BigDecimal monthlyBudget, BigDecimal monthlySaving, BigDecimal savingsRate, BigDecimal runningBalance, BigDecimal largestExpense, Category largestSpendingCategory) {
        this.budget = budget;
        this.monthlyIncome = monthlyIncome;
        this.monthlySpending = monthlySpending;
        this.monthlyBudget = monthlyBudget;
        this.monthlySaving = monthlySaving;
        this.savingsRate = savingsRate;
        this.runningBalance = runningBalance;
        this.largestExpense = largestExpense;
        this.largestSpendingCategory = largestSpendingCategory;
    }

    public List<Budget> getBudget() {
        return budget;
    }

    public void setBudget(List<Budget> budget) {
        this.budget = budget;
    }

    public BigDecimal getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(BigDecimal monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public BigDecimal getMonthlySpending() {
        return monthlySpending;
    }

    public void setMonthlySpending(BigDecimal monthlySpending) {
        this.monthlySpending = monthlySpending;
    }

    public BigDecimal getMonthlyBudget() {
        return monthlyBudget;
    }

    public void setMonthlyBudget(BigDecimal monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }

    public BigDecimal getMonthlySaving() {
        return monthlySaving;
    }

    public void setMonthlySaving(BigDecimal monthlySaving) {
        this.monthlySaving = monthlySaving;
    }

    public BigDecimal getSavingsRate() {
        return savingsRate;
    }

    public void setSavingsRate(BigDecimal savingsRate) {
        this.savingsRate = savingsRate;
    }

    public BigDecimal getRunningBalance() {
        return runningBalance;
    }

    public void setRunningBalance(BigDecimal runningBalance) {
        this.runningBalance = runningBalance;
    }

    public Category getLargestSpendingCategory() {
        return largestSpendingCategory;
    }

    public void setLargestSpendingCategory(Category largestSpendingCategory) {
        this.largestSpendingCategory = largestSpendingCategory;
    }

    public BigDecimal getLargestExpense() {
        return largestExpense;
    }

    public void setLargestExpense(BigDecimal largestExpense) {
        this.largestExpense = largestExpense;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        FinancialSummary that = (FinancialSummary) object;
        return Objects.equals(budget, that.budget) && Objects.equals(monthlyIncome, that.monthlyIncome) && Objects.equals(monthlySpending, that.monthlySpending) && Objects.equals(monthlyBudget, that.monthlyBudget) && Objects.equals(monthlySaving, that.monthlySaving) && Objects.equals(savingsRate, that.savingsRate) && Objects.equals(runningBalance, that.runningBalance) && Objects.equals(largestExpense, that.largestExpense) && largestSpendingCategory == that.largestSpendingCategory;
    }

    @Override
    public int hashCode() {
        return Objects.hash(budget, monthlyIncome, monthlySpending, monthlyBudget, monthlySaving, savingsRate, runningBalance, largestExpense, largestSpendingCategory);
    }
}
