package com.project.fintrackapi.service;

import com.project.fintrackapi.dto.CreateAccountRequest;
import com.project.fintrackapi.dto.UpdateAccountRequest;
import com.project.fintrackapi.entity.Account;
import com.project.fintrackapi.entity.Budget;
import com.project.fintrackapi.exception.ResourceNotFoundException;
import com.project.fintrackapi.repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
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

    public void deleteAccount(Long id) {
        if (!accountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Account with ID " + id + " not found");
        }
        accountRepository.deleteById(id);
    }
}
