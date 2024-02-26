package com.garrech.bankmanagement.Repositories;

import com.garrech.bankmanagement.entities.Client;
import com.garrech.bankmanagement.entities.ClientInfo;
import com.garrech.bankmanagement.utils.ClientType;
import com.garrech.bankmanagement.utils.OperationType;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Repository
@AllArgsConstructor
public class ClientRepositoryImpl implements ClientRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public int save(Client client) {
        String sql = "INSERT INTO client (clientName, password, accountId,clientType) VALUES ( ?,?, ?, ?)";
        return jdbcTemplate.update(sql, client.getClientName(), client.getPassword(), client.getAccountId(), client.getClientType().name());
    }

    @Override
    public Optional<Client> findClientByName(String clientName) {
        String sql = "SELECT * FROM client WHERE clientName = ?";
        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) {
                return Optional.of(Client.builder()
                        .clientId(rs.getLong("clientId"))
                        .clientName(rs.getString("clientName"))
                        .password(rs.getString("password"))
                        .accountId(rs.getLong("accountId"))
                        .clientType(ClientType.valueOf(rs.getString("clientType")))
                        .build());
            } else {
                return Optional.empty();
            }
        }, clientName);
    }

    public List<ClientInfo> searchClients(Supplier<String> sqlFilterBuilder) {
        String searchSql = "SELECT c.clientId , c.clientName, c.clientType, " +
                "a.accountId , a.accountAmount, " +
                "o.operationId, o.operationAmount, o.operationType, o.date " +
                "FROM client c " +
                "LEFT JOIN account a ON c.accountId = a.accountId " +
                "LEFT JOIN operation o ON c.accountId = o.accountId";

        searchSql += sqlFilterBuilder.get();

        return jdbcTemplate.query(searchSql, (rs, rowNum) -> {
            ClientInfo.ClientInfoBuilder clientInfoBuilder = ClientInfo.builder()
                    .clientId(rs.getLong("clientId"))
                    .clientName(rs.getString("clientName"))
                    .clientType(ClientType.valueOf(rs.getString("clientType")))
                    .accountId(rs.getLong("accountId"))
                    .accountAmount((Double) rs.getObject("accountAmount"));

            Long operationId = (Long) rs.getObject("operationId");
            if (operationId != null) {
                clientInfoBuilder.operationId((Long) rs.getObject("operationId"))
                        .operationAmount((Double) rs.getObject("operationAmount"))
                        .operationType(OperationType.valueOf(rs.getString("operationType")))
                        .operationDate(rs.getTimestamp("date").toLocalDateTime());
            }
            return clientInfoBuilder.build();
        });
    }
}
