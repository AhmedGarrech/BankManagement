package com.garrech.bankmanagement.Repositories;

import com.garrech.bankmanagement.entities.Account;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface AccountRepository {

    Long save(Account account);

    public Long update(Account account);
    Optional<Account> findAccountById(Long accountId);
}
