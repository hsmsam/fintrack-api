package com.project.fintrackapi;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Entity
public class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private Category category;
    private LocalDate datePaid;
    @Enumerated(EnumType.STRING)
    private Frequency frequency;
    private String description;

    public Expense() {

    }

    public Expense(BigDecimal amount, Category category, LocalDate datePaid, Frequency frequency, String description) {
        this.amount = amount;
        this.category = category;
        this.datePaid = datePaid;
        this.frequency = frequency;
        this.description = description;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public LocalDate getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(LocalDate datePaid) {
        this.datePaid = datePaid;
    }

    public Frequency getFrequency() {
        return frequency;
    }

    public void setFrequency(Frequency frequency) {
        this.frequency = frequency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Expense expense = (Expense) object;
        return Objects.equals(id, expense.id) && Objects.equals(account, expense.account) && Objects.equals(amount, expense.amount) && category == expense.category && Objects.equals(datePaid, expense.datePaid) && frequency == expense.frequency && Objects.equals(description, expense.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, account, amount, category, datePaid, frequency, description);
    }
}
