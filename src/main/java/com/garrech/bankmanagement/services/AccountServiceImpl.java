package com.garrech.bankmanagement.services;

import com.garrech.bankmanagement.Repositories.AccountRepository;
import com.garrech.bankmanagement.configurations.exceptions.CrudException;
import com.garrech.bankmanagement.entities.Account;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Long save(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Long update(Account account) {
        return accountRepository.update(account);
    }

    @Override
    public List<Account> findAllAccounts() {
        return null;
    }

    @Override
    public Account findAccountById(Long accountId) {
        Optional<Account> optionalAccount = accountRepository.findAccountById(accountId);
        if (optionalAccount.isPresent()) {
            return optionalAccount.get();
        }
        throw new CrudException("Account not found ", HttpStatus.NOT_FOUND);
    }
}
