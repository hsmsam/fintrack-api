package com.project.fintrackapi.service;

import com.project.fintrackapi.dto.CreateAccountRequest;
import com.project.fintrackapi.dto.UpdateAccountRequest;
import com.project.fintrackapi.entity.Account;
import com.project.fintrackapi.exception.ResourceNotFoundException;
import com.project.fintrackapi.repository.AccountRepository;
import com.project.fintrackapi.repository.BudgetRepository;
import com.project.fintrackapi.repository.ExpenseRepository;
import com.project.fintrackapi.repository.IncomeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final BudgetRepository budgetRepository;

    public AccountService(AccountRepository accountRepository, IncomeRepository incomeRepository, ExpenseRepository expenseRepository, BudgetRepository budgetRepository) {
        this.accountRepository = accountRepository;
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.budgetRepository = budgetRepository;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account with ID " + id + " not found"));
    }

    public void addAccount(CreateAccountRequest request) {
        Account account = new Account(request.name(), request.email());
        accountRepository.save(account);
    }

    public void updateAccount(Long id, UpdateAccountRequest request) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account with ID " + id + " not found"));

        account.setName(request.name());
        account.setEmail(request.email());

        accountRepository.save(account);
    }

    @Transactional
    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Account with ID " + id + " not found");
        }
        incomeRepository.deleteByAccountId(id);
        expenseRepository.deleteByAccountId(id);
        budgetRepository.deleteByAccountId(id);
        accountRepository.deleteById(id);
    }
}
