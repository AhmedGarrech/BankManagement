package com.garrech.bankmanagement.Repositories;

import com.garrech.bankmanagement.entities.Account;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Long save(Account account) {
        String sql = "INSERT INTO account (accountAmount) VALUES (?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setDouble(1, account.getAccountAmount());
            return preparedStatement;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    @Override
    public Long update(Account account) {
        String sql = "UPDATE account SET accountAmount = ? WHERE accountId = ?";

        jdbcTemplate.update(sql, account.getAccountAmount(), account.getAccountId());

        return account.getAccountId();
    }

    @Override
    public Optional<Account> findAccountById(Long accountId) {
        String sql = "SELECT accountId, accountAmount FROM account WHERE accountId = ?";
        return jdbcTemplate.query(sql, resultSet -> {
            if (resultSet.next()) {
                return Optional.of(Account.builder()
                        .accountId(resultSet.getLong("accountId"))
                        .accountAmount(resultSet.getDouble("accountAmount"))
                        .build());
            } else {
                return Optional.empty();
            }
        },accountId);
    }
}
