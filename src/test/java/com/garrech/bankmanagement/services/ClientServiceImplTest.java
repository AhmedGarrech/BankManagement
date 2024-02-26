package com.garrech.bankmanagement.services;

import com.garrech.bankmanagement.Repositories.AccountRepository;
import com.garrech.bankmanagement.Repositories.ClientRepository;
import com.garrech.bankmanagement.configurations.exceptions.CrudException;
import com.garrech.bankmanagement.entities.Account;
import com.garrech.bankmanagement.entities.Client;
import com.garrech.bankmanagement.entities.ClientInfo;
import com.garrech.bankmanagement.utils.ClientType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class ClientServiceImplTest {

    @Captor
    private ArgumentCaptor<Account> accountCaptor;
    @Captor
    private ArgumentCaptor<Client> clientCaptor;

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private BCryptPasswordEncoder encoder;

    @InjectMocks
    private ClientServiceImpl clientService;

    @BeforeEach
    public void setUp() {
        when(encoder.encode(anyString())).thenReturn("encodedPassword");
        when(accountRepository.save(any())).thenReturn(1L);
    }

    @Test
    public void Save_DelegatesPasswordToEncoder() {
        Client client = Client.builder()
                .clientName("Ahmed")
                .password("password")
                .clientType(ClientType.CLIENT)
                .build();

        clientService.save(client);

        verify(encoder).encode("password");
    }

    @Test
    public void Save_DelegatesAccountToAccountRepository() {
        Client client = Client.builder()
                .clientName("Ahmed")
                .password("password")
                .clientType(ClientType.CLIENT)
                .build();

        clientService.save(client);

        verify(accountRepository).save(accountCaptor.capture());
        Account savedAccount = accountCaptor.getValue();

        assertNotNull(savedAccount);
        assertEquals(0.0, savedAccount.getAccountAmount());
    }

    @Test
    public void Save_DelegatesClientToClientRepository() {
        Client client = Client.builder()
                .clientName("Ahmed")
                .password("password")
                .clientType(ClientType.CLIENT)
                .build();
        when(accountRepository.save(any())).thenReturn(123L);

        clientService.save(client);

        verify(clientRepository).save(clientCaptor.capture());
        Client savedClient = clientCaptor.getValue();

        assertNotNull(savedClient);
        assertEquals(savedClient.getClientName(), client.getClientName());
        assertEquals(savedClient.getClientType(), client.getClientType());
        assertEquals(savedClient.getPassword(), client.getPassword());
        assertEquals(savedClient.getAccountId(), 123L);
    }

    @Test
    public void SaveClient_ReturnsSuccess() {
        Client client = Client.builder()
                .clientName("Ahmed")
                .password("password")
                .clientType(ClientType.CLIENT)
                .build();


        clientService.save(client);

        verify(encoder).encode("password");
        verify(accountRepository).save(any());
        verify(clientRepository).save(client);
    }

    @Test
    public void findAllClients_ClientRepositoryInvocation() {

        List<ClientInfo> clients = Arrays.asList(
                ClientInfo.builder().clientId(1L).clientName("Ahmed").build(),
                ClientInfo.builder().clientId(2L).clientName("Mohamed").build()
        );

        clientService.findAllClients();

        verify(clientRepository, times(1)).searchClients(any());
    }

    @Test
    public void findAllClients_ReturnsSuccess() {

        List<ClientInfo> clients = Arrays.asList(
                ClientInfo.builder().clientId(1L).clientName("Ahmed").build(),
                ClientInfo.builder().clientId(2L).clientName("Mohamed").build()
        );

        when(clientRepository.searchClients(any())).thenReturn(clients);

        List<ClientInfo> foundClients = clientService.findAllClients();

        assertEquals(clients.size(), foundClients.size());
        assertTrue(foundClients.containsAll(clients));
    }

    @Test
    public void findClientByName_ClientRepositoryInvocation() {

        String clientName = "Ahmed";
        Client client = Client.builder().clientId(1L).clientName(clientName).build();
        when(clientRepository.findClientByName(clientName)).thenReturn(Optional.of(client));

        clientService.findClientByName(clientName);

        verify(clientRepository, times(1)).findClientByName(clientName);
    }

    @Test
    public void findClientByName_ClientFound() {

        String clientName = "Ahmed";
        Client client = Client.builder().clientId(1L).clientName(clientName).build();
        when(clientRepository.findClientByName(clientName)).thenReturn(Optional.of(client));

        Client foundClient = clientService.findClientByName(clientName);

        assertNotNull(foundClient);
        assertEquals(client, foundClient);
    }

    @Test
    public void findClientByName_ClientNotFound() {
        String clientName = "Ahmed";
        when(clientRepository.findClientByName(clientName)).thenReturn(Optional.empty());

        assertThrows(CrudException.class, () -> clientService.findClientByName(clientName));
    }

    @Test
    public void searchClients_ClientRepositoryInvocation() {
        Map<String, String> filter = new HashMap<>();
        filter.put("clientType", "CLIENT");
        List<ClientInfo> clients = Arrays.asList(
                ClientInfo.builder().clientId(1L).clientName("Ahmed").clientType(ClientType.CLIENT).build(),
                ClientInfo.builder().clientId(2L).clientName("Mohamed").clientType(ClientType.CLIENT).build()
        );
        when(clientRepository.searchClients(any())).thenReturn(clients);

        clientService.searchClients(filter);

        verify(clientRepository, times(1)).searchClients(any());
    }

    @Test
    public void searchClients_ReturnsClientListSuccess() {
        Map<String, String> filter = new HashMap<>();
        filter.put("clientType", "CLIENT");
        List<ClientInfo> clients = Arrays.asList(
                ClientInfo.builder().clientId(1L).clientName("Ahmed").clientType(ClientType.CLIENT).build(),
                ClientInfo.builder().clientId(2L).clientName("Mohamed").clientType(ClientType.CLIENT).build()
        );
        when(clientRepository.searchClients(any())).thenReturn(clients);

        List<ClientInfo> foundClients = clientService.searchClients(filter);

        assertEquals(clients.size(), foundClients.size());
        assertTrue(foundClients.containsAll(clients));
    }
}