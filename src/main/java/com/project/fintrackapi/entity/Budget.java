package com.project.fintrackapi.entity;

import com.project.fintrackapi.enums.Category;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Objects;

@Entity
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    @Enumerated(EnumType.STRING)
    private Category budgetCategory;
    private BigDecimal budgetTotal;
    private java.time.YearMonth budgetMonth;

    public Budget() {

    }

    public Budget(Category budgetCategory, BigDecimal budgetTotal, YearMonth budgetMonth) {
        this.budgetCategory = budgetCategory;
        this.budgetTotal = budgetTotal;
        this.budgetMonth = budgetMonth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Category getBudgetCategory() {
        return budgetCategory;
    }

    public void setBudgetCategory(Category budgetCategory) {
        this.budgetCategory = budgetCategory;
    }

    public BigDecimal getBudgetTotal() {
        return budgetTotal;
    }

    public void setBudgetTotal(BigDecimal budgetTotal) {
        this.budgetTotal = budgetTotal;
    }

    public YearMonth getBudgetMonth() {
        return budgetMonth;
    }

    public void setBudgetMonth(YearMonth budgetMonth) {
        this.budgetMonth = budgetMonth;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Budget budget = (Budget) object;
        return Objects.equals(id, budget.id) && Objects.equals(account, budget.account) && budgetCategory == budget.budgetCategory && Objects.equals(budgetTotal, budget.budgetTotal) && budgetMonth == budget.budgetMonth;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, account, budgetCategory, budgetTotal, budgetMonth);
    }
}
