package com.garrech.bankmanagement.services;

import com.garrech.bankmanagement.Repositories.AccountRepository;
import com.garrech.bankmanagement.Repositories.ClientRepository;
import com.garrech.bankmanagement.configurations.exceptions.CrudException;
import com.garrech.bankmanagement.entities.Account;
import com.garrech.bankmanagement.entities.Client;
import com.garrech.bankmanagement.entities.ClientInfo;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public Optional<String> save(Client client) {
        client.setPassword(encoder.encode(client.getPassword()));
        client.setAccountId(saveNewAccount());
        int affectedRows = clientRepository.save(client);

//        if (affectedRows <= 0) {
//            return Optional.of(client.getClientName());
//        }
        return Optional.empty();
    }

    @Override
    public List<ClientInfo> findAllClients() {
        return clientRepository.searchClients(() -> "");
    }

    @Override
    public Client findClientByName(String clientName) {
        Optional<Client> optionalClient = clientRepository.findClientByName(clientName);
        if (optionalClient.isPresent()) {
            return optionalClient.get();
        }
        throw new CrudException("Client not found", HttpStatus.NOT_FOUND);
    }

    @Override
    public List<ClientInfo> searchClients(Map<String, String> filterClient) {
        StringBuilder filterQuery = new StringBuilder();
        filterClient.forEach((key, value) -> {
            if (!filterQuery.isEmpty()) {
                filterQuery.append(" AND ");
            }
            filterQuery.append(key).append(" = '").append(value).append("' ");
        });

        return getSearchedClients(filterQuery::toString);
    }

    private List<ClientInfo> getSearchedClients(Supplier<String> filterQuery) {
        return clientRepository.searchClients(filterQuery);
    }

    private Long saveNewAccount() {
        return accountRepository.save(Account.builder()
                .accountAmount(0.0)
                .build());
    }
}