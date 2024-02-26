package com.garrech.bankmanagement.configurations.jobs;

import com.garrech.bankmanagement.entities.Client;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class ClientProcessor {

    public ItemProcessor<Client, Client> processor() {
        return client -> client;
    }
}
