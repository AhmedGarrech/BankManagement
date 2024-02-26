package com.garrech.bankmanagement.services;

import com.garrech.bankmanagement.entities.Client;
import com.garrech.bankmanagement.entities.ClientInfo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ClientService {
    Optional<String> save(Client client);

    List<ClientInfo> findAllClients();

    Client findClientByName(String clientName);

    List<ClientInfo> searchClients(Map<String, String> filterClient);
}
