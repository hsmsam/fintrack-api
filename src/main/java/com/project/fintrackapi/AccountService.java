package com.project.fintrackapi;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("ID not found"));
    }

    public void addAccount(Account account) {
        accountRepository.save(account);
    }

    public void updateAccount(Long id, Account account) {
        Account accountExist = accountRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("ID not found"));

        if (account.getName() != null) {
            accountExist.setName(account.getName());
        }

        if (account.getEmail() != null) {
            accountExist.setEmail(account.getEmail());
        }


    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }
}
