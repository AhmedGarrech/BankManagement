package com.garrech.bankmanagement.Repositories;

import com.garrech.bankmanagement.entities.Operation;
import com.garrech.bankmanagement.utils.OperationType;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class OperationRepositoryImpl implements OperationRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void save(Operation operation) {
        String sql = "INSERT INTO operation (accountId, operationAmount, operationType, date) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql, operation.getAccountId(), operation.getOperationAmount(), operation.getOperationType().name(), operation.getDate());
    }

    @Override
    public List<Operation> findAllOperationsByAccountId(Long accountId) {
        String sql = "SELECT * FROM operation WHERE accountId = ?";
        return jdbcTemplate.query(sql, new Object[]{accountId}, (rs, rowNum) ->
                Operation.builder()
                        .operationId(rs.getLong("operationId"))
                        .accountId(rs.getLong("accountId"))
                        .operationAmount(rs.getDouble("operationAmount"))
                        .operationType(OperationType.valueOf(rs.getString("operationType")))
                        .date(rs.getTimestamp("date").toLocalDateTime())
                        .build()
        );
    }
}
