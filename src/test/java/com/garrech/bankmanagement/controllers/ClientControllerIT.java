package com.garrech.bankmanagement.controllers;

import com.garrech.bankmanagement.configurations.restApis.Api;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(value = {"/schema-test.sql", "/data-test.sql"} ,executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class ClientControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll

    public static void setUp(){

    }

    @Test
    public void save_BatchServiceInvocation() throws Exception {
        mockMvc.perform(post(Api.ClientsApi.clientsApi)
                        .content("{\n" +
                                "    \"clientFilePath\": \"./humain1.csv\"\n" +
                                "}"))
                .andExpect(status().isOk());
    }

    @Test
    public void findAllClients_ReturnClientListSuccess() throws Exception {
        mockMvc.perform(get(Api.ClientsApi.clientsApi))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].clientId").value(1L))
                .andExpect(jsonPath("$[0].clientName").value("admin"))
                .andExpect(jsonPath("$[0].accountId").value(1L))
                .andExpect(jsonPath("$[0].clientType").value("ADMIN"));
    }

    @Test
    public void searchClients_ExistingClient_ReturnClientListSuccess() throws Exception {
        mockMvc.perform(get(Api.ClientsApi.searchClientsApi + "?clientName=admin"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].clientId").value(1L))
                .andExpect(jsonPath("$[0].clientName").value("admin"))
                .andExpect(jsonPath("$[0].accountId").value(1L))
                .andExpect(jsonPath("$[0].clientType").value("ADMIN"));
    }

    @Test
    public void searchClients_ClientDoesNotExist_ReturnEmptyClientListSuccess() throws Exception {
        mockMvc.perform(get(Api.ClientsApi.searchClientsApi + "?clientName=ahmed"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }
}