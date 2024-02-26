package com.garrech.bankmanagement.controllers;

import com.garrech.bankmanagement.configurations.restApis.Api;
import com.garrech.bankmanagement.entities.ClientInfo;
import com.garrech.bankmanagement.services.BatchService;
import com.garrech.bankmanagement.services.ClientService;
import com.garrech.bankmanagement.utils.ClientType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;
    @MockBean
    private BatchService batchService;

    @Test
    public void save_BatchServiceInvocation() throws Exception {

        mockMvc.perform(post(Api.ClientsApi.clientsApi)
                        .content("{\n" +
                                "    \"clientFilePath\": \"./humain1.csv\"\n" +
                                "}"))
                .andExpect(status().isOk());

        verify(batchService, times(1)).runSaveClientBatchJob(any());
    }

    @Test
    public void findAllClients_ClientServiceInvocation() throws Exception {
        when(clientService.findAllClients()).thenReturn(
                Collections.singletonList(ClientInfo.builder()
                        .clientId(1L)
                        .clientName("Ahmed")
                        .clientType(ClientType.CLIENT)
                        .build()));

        mockMvc.perform(get(Api.ClientsApi.clientsApi)
                        .with(httpBasic("admin", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(clientService, times(1)).findAllClients();
    }

    @Test
    public void findAllClients_ReturnClientListSuccess() throws Exception {
        ClientInfo client = ClientInfo.builder().clientId(1L).clientName("Ahmed").clientType(ClientType.CLIENT).accountId(1L).build();

        when(clientService.findAllClients()).thenReturn(Collections.singletonList(client));

        mockMvc.perform(get(Api.ClientsApi.clientsApi)
                        .with(httpBasic("admin", "password")))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].clientId").value(client.getClientId()))
                .andExpect(jsonPath("$[0].clientName").value(client.getClientName()))
                .andExpect(jsonPath("$[0].accountId").value(client.getAccountId()))
                .andExpect(jsonPath("$[0].clientType").value(client.getClientType().name()));
    }

    @Test
    public void searchClients_ClientServiceInvocation() throws Exception {
        when(clientService.searchClients(any())).thenReturn(
                Collections.singletonList(ClientInfo.builder()
                        .clientId(1L)
                        .clientName("Ahmed")
                        .clientType(ClientType.CLIENT)
                        .build()));

        mockMvc.perform(get(Api.ClientsApi.searchClientsApi))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(clientService, times(1)).searchClients(any());
    }

    @Test
    public void searchClients_ReturnClientListSuccess() throws Exception {
        ClientInfo client = ClientInfo.builder().clientId(1L).clientName("Ahmed").clientType(ClientType.CLIENT).accountId(1L).build();

        when(clientService.searchClients(any())).thenReturn(Collections.singletonList(client));

        mockMvc.perform(get(Api.ClientsApi.searchClientsApi))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].clientId").value(client.getClientId()))
                .andExpect(jsonPath("$[0].clientName").value(client.getClientName()))
                .andExpect(jsonPath("$[0].accountId").value(client.getAccountId()))
                .andExpect(jsonPath("$[0].clientType").value(client.getClientType().name()));
    }
}