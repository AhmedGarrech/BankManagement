package com.garrech.bankmanagement.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.garrech.bankmanagement.configurations.restApis.Api;
import com.garrech.bankmanagement.entities.Operation;
import com.garrech.bankmanagement.utils.OperationType;
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
@Sql(scripts = "/schema-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
class OperationControllerIT {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void save_ReturnSuccess() throws Exception {
        mockMvc.perform(post(Api.OperationsApi.operationApi)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(anyJsonDepositOperation()))
                .andExpect(status().isOk());
    }

    @Test
    public void save_ReturnFailed() throws Exception {
        mockMvc.perform(post(Api.OperationsApi.operationApi)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(anyJsonWithdrawalOperation()))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Operation Refused: Insufficient funds"));
    }


    @Test
    public void findOperationsByAccountId_ReturnOperationListSuccess() throws Exception {
        mockMvc.perform(get(Api.OperationsApi.operationApi + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].operationId").value(1L))
                .andExpect(jsonPath("$[0].operationAmount").value(100.0))
                .andExpect(jsonPath("$[0].accountId").value(1L))
                .andExpect(jsonPath("$[0].operationType").value(OperationType.DEPOSIT.name()));
    }

    private String anyJsonDepositOperation() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        Operation operation = Operation.builder()
                .operationId(1L)
                .accountId(1L)
                .operationAmount(100.0)
                .operationType(OperationType.DEPOSIT)
                .build();

        return objectMapper.writeValueAsString(operation);
    }

    private String anyJsonWithdrawalOperation() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        Operation operation = Operation.builder()
                .operationId(1L)
                .accountId(1L)
                .operationAmount(500.0)
                .operationType(OperationType.WITHDRAWAL)
                .build();

        return objectMapper.writeValueAsString(operation);
    }
}