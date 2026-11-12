package com.project.fintrackapi.entity;

import com.project.fintrackapi.enums.Frequency;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    @NotNull(message = "Income must belong to and have an account")
    private Account account;
    @Positive(message = "Income amount must be greater than zero")
    @NotNull(message = "Income amount must have a value")
    private BigDecimal amount;
    @NotBlank(message = "Income must include the name of the source of income")
    private String source;
    @NotNull(message = "Income must include a date for when it's received")
    private LocalDate dateReceived;
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Income must include a frequency")
    private Frequency frequency;
    private String description;

    public Income() {

    }

    public Income(BigDecimal amount, String source, LocalDate dateReceived, Frequency frequency, String description) {
        this.amount = amount;
        this.source = source;
        this.dateReceived = dateReceived;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public LocalDate getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(LocalDate dateReceived) {
        this.dateReceived = dateReceived;
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
        Income income = (Income) object;
        return Objects.equals(id, income.id) && Objects.equals(account, income.account) && Objects.equals(amount, income.amount) && Objects.equals(source, income.source) && Objects.equals(dateReceived, income.dateReceived) && frequency == income.frequency && Objects.equals(description, income.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, account, amount, source, dateReceived, frequency, description);
    }
}
