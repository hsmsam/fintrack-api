package com.project.fintrackapi.service;

import com.project.fintrackapi.dto.CreateIncomeRequest;
import com.project.fintrackapi.dto.UpdateIncomeRequest;
import com.project.fintrackapi.entity.Account;
import com.project.fintrackapi.entity.Income;
import com.project.fintrackapi.exception.ResourceNotFoundException;
import com.project.fintrackapi.repository.AccountRepository;
import com.project.fintrackapi.repository.IncomeRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;

@Service
public class IncomeService {
    private final IncomeRepository incomeRepository;
    private final AccountRepository accountRepository;

    public IncomeService(IncomeRepository incomeRepository, AccountRepository accountRepository) {
        this.incomeRepository = incomeRepository;
        this.accountRepository = accountRepository;
    }

    public List<Income> getAllIncomes() {
        return incomeRepository.findAll();
    }

    public Income getIncomeById(Long id) {
        return incomeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Income with ID " + id + " not found"));
    }

    public List<Income> getIncomeByAccountId(Long id) {
        return incomeRepository.findByAccountId(id);
    }

    public BigDecimal getTotalIncomeThisMonth(Long id, YearMonth yearMonth) {
        return getIncomeByAccountId(id).stream()
                .filter(income -> YearMonth.from(income.getDateReceived()).equals(yearMonth))
                .map(Income::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addIncome(CreateIncomeRequest request) {
        Account account = accountRepository.findById(request.accountId())
                .orElseThrow(() -> new ResourceNotFoundException("Income with ID " + request.accountId() + " not found"));

        Income income = new Income(request.amount(), request.source(), request.dateReceived(), request.frequency(), request.description());
        income.setAccount(account);

        incomeRepository.save(income);
    }

    public void updateIncome(Long id, UpdateIncomeRequest request) {
        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Income with ID " + id + " not found"));

        income.setAmount(request.amount());
        income.setSource(request.source());
        income.setDateReceived(request.dateReceived());
        income.setFrequency(request.frequency());
        income.setDescription(request.description());

        incomeRepository.save(income);
    }

    public void deleteIncome(Long id) {
        if (!incomeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Income with ID " + id + " not found");
        }
        incomeRepository.deleteById(id);
    }
}
