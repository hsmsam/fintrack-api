package com.project.fintrackapi.controller;

import com.project.fintrackapi.dto.CreateAccountRequest;
import com.project.fintrackapi.dto.UpdateAccountRequest;
import com.project.fintrackapi.service.AccountService;
import com.project.fintrackapi.entity.Account;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public Page<Account> getAllAccounts(@PageableDefault(sort = "id") Pageable page) {
        return accountService.getAllAccounts(page);
    }

    @GetMapping("{id}")
    public Account getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    @PostMapping
    public void addAccount(@Valid @RequestBody CreateAccountRequest request) {
        accountService.addAccount(request);
    }

    @PutMapping("{id}")
    public void updateAccount(@PathVariable Long id, @Valid @RequestBody UpdateAccountRequest request) {
        accountService.updateAccount(id, request);
    }

    @DeleteMapping("{id}")
    public void deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
    }
}
