package com.project.fintrackapi;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;

@Service
public class IncomeService {
    private final IncomeRepository incomeRepository;

    public IncomeService(IncomeRepository incomeRepository) {
        this.incomeRepository = incomeRepository;
    }

    public List<Income> getAllIncomes() {
        return incomeRepository.findAll();
    }

    public Income getIncomeById(Long id) {
        return incomeRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("ID not found"));
    }

    public List<Income> getIncomeByAccountId(Long id) {
        return incomeRepository.findByAccountId(id);
    }

    public BigDecimal getTotalIncomeThisMonth(Long id, Month month) {
        return getIncomeByAccountId(id).stream()
                .filter(expense -> expense.getDateReceived().getMonth() == month)
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addIncome(Income income) {
        incomeRepository.save(income);
    }

    public void updateIncome(Long id, Income income) {
        Income incomeExists = incomeRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("ID not found"));

        if (income.getAmount() != null) {
            incomeExists.setAmount(income.getAmount());
        }

        if (income.getSource() != null) {
            incomeExists.setSource(income.getSource());
        }

        if (income.getDateReceived() != null) {
            incomeExists.setDateReceived(income.getDateReceived());
        }

        if (income.getFrequency() != null) {
            incomeExists.setFrequency(income.getFrequency());
        }

        if (income.getDescription() != null) {
            incomeExists.setDescription(income.getDescription());
        }

        incomeRepository.save(incomeExists);
    }

    public void deleteIncome(Long id) {
        incomeRepository.deleteById(id);
    }
}
