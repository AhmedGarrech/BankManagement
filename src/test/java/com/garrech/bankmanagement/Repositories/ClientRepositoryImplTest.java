package com.garrech.bankmanagement.Repositories;

import com.garrech.bankmanagement.entities.Client;
import com.garrech.bankmanagement.entities.ClientInfo;
import com.garrech.bankmanagement.utils.ClientType;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class ClientRepositoryImplTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ClientRepositoryImpl clientRepository;

    @Test
    public void save_ReturnsSuccess() {
        Client client = Client.builder()
                .clientName("Ahmed")
                .password("password")
                .accountId(1L)
                .clientType(ClientType.CLIENT)
                .build();

        clientRepository.save(client);

        verify(jdbcTemplate).update(
                anyString(),
                eq("Ahmed"),
                eq("password"),
                eq(1L),
                eq(ClientType.CLIENT.name()));
    }

    @Test
    public void testFindAllClients_Success() {
        List<ClientInfo> rows = new ArrayList<>();
        rows.add(ClientInfo.builder()
                .clientId(1L)
                .clientName("Ahmed")
                .accountId(1L)
                .clientType(ClientType.CLIENT)
                .build());
        rows.add(ClientInfo.builder()
                .clientId(2L)
                .clientName("Mohamed")
                .accountId(2L)
                .clientType(ClientType.ADMIN)
                .build());
        when(jdbcTemplate.query(anyString(), any(RowMapper.class))).thenReturn(rows);

        List<ClientInfo> clients = clientRepository.searchClients(() -> "");

        assertEquals(2, clients.size());
        assertEquals("Ahmed", clients.get(0).getClientName());
        assertEquals(1L, clients.get(0).getAccountId());
        assertEquals(ClientType.CLIENT, clients.get(0).getClientType());
        assertEquals("Mohamed", clients.get(1).getClientName());
        assertEquals(2L, clients.get(1).getAccountId());
        assertEquals(ClientType.ADMIN, clients.get(1).getClientType());
    }

    @Test
    public void testFindClientByName_ClientFound() {
        String clientName = "Ahmed";
        Client clientAhmed = Client.builder()
                .clientId(1L)
                .clientName(clientName)
                .password("password")
                .accountId(1L)
                .clientType(ClientType.CLIENT)
                .build();
        when(jdbcTemplate.query(anyString(), any(ResultSetExtractor.class), eq(clientName)))
                .thenReturn(Optional.of(clientAhmed));

        Optional<Client> optionalClient = clientRepository.findClientByName(clientName);

        assertTrue(optionalClient.isPresent());
        Client client = optionalClient.get();
        assertEquals("Ahmed", client.getClientName());
        assertEquals("password", client.getPassword());
        assertEquals(1L, client.getAccountId());
        assertEquals(ClientType.CLIENT, client.getClientType());
    }

    @Test
    public void testFindClientByName_ClientNotFound() {

        String clientName = "Ahmed";
        when(jdbcTemplate.query(anyString(), any(ResultSetExtractor.class), eq(clientName)))
                .thenReturn(Optional.empty());

        Optional<Client> optionalClient = clientRepository.findClientByName(clientName);

        assertFalse(optionalClient.isPresent());
    }
}