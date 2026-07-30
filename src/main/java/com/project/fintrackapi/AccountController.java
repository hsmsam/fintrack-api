package com.project.fintrackapi;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/account")
public class AccountController {
    AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("{id}")
    public Account getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    @PutMapping("{id}")
    public void updateAccount(@PathVariable Long id, @RequestBody Account account) {
        accountService.updateAccount(id, account);
    }

    @PostMapping
    public void addAccount(@RequestBody Account account) {
        accountService.addAccount(account);
    }

    @DeleteMapping("{id}")
    public void deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
    }
}
