package com.garrech.bankmanagement.Repositories;

import com.garrech.bankmanagement.entities.Client;
import com.garrech.bankmanagement.entities.ClientInfo;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Repository
public interface ClientRepository {
    int save(Client client);

    Optional<Client> findClientByName(String clientName);
    List<ClientInfo> searchClients(Supplier<String> sqlFilterBuilder);
}
