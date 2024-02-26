package com.garrech.bankmanagement.configurations.jobs;

import com.garrech.bankmanagement.configurations.exceptions.CrudException;
import com.garrech.bankmanagement.entities.Client;
import com.garrech.bankmanagement.services.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class ClientWriter {

    private ClientService clientService;

    public ItemWriter<Client> writer() {
        List<String> unsavedClient = new ArrayList<>();
        ItemWriter<Client> clientItemWriter = clients -> {
            for (Client client : clients) {
                Optional<String> clientOptional = clientService.save(client);
                clientOptional.ifPresent(unsavedClient::add);
            }
        };

        if (!unsavedClient.isEmpty()) {
            throw new CrudException(getUnsavedClient(unsavedClient), HttpStatus.CONFLICT);
        }
        return clientItemWriter;
    }

    private String getUnsavedClient(List<String> unsavedClient) {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < unsavedClient.size(); i++) {
            result.append(unsavedClient.get(i));
            if (i < unsavedClient.size() - 1) {
                result.append(" , ");
            }
        }
        result.append("]");
        return result.toString();
    }
}
