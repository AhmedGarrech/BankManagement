package com.garrech.bankmanagement.services;

import com.garrech.bankmanagement.entities.Account;

import java.util.List;

public interface AccountService {
    Long save(Account account);

    Long update(Account account);

    List<Account> findAllAccounts();

    Account findAccountById(Long accountId);
}
