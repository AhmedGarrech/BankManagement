package com.garrech.bankmanagement.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.garrech.bankmanagement.configurations.restApis.Api;
import com.garrech.bankmanagement.entities.Operation;
import com.garrech.bankmanagement.services.OperationService;
import com.garrech.bankmanagement.services.OperationTransactionService;
import com.garrech.bankmanagement.utils.OperationType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class OperationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OperationService operationService;
    @MockBean
    private OperationTransactionService operationTransactionService;

    @Test
    public void findOperationsByAccountId_OperationServiceInvocation() throws Exception {
        when(operationService.findAllOperationsByAccountId(anyLong())).thenReturn(
                Collections.singletonList(Operation.builder()
                        .operationId(1L)
                        .accountId(1L)
                        .operationAmount(100.0)
                        .date(LocalDateTime.of(2024, 2, 25, 9, 10))
                        .operationType(OperationType.DEPOSIT)
                        .build()));

        mockMvc.perform(get(Api.OperationsApi.operationApi + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(operationService, times(1)).findAllOperationsByAccountId(anyLong());
    }

    @Test
    public void findOperationsByAccountId_ReturnOperationListSuccess() throws Exception {
        Operation operation = Operation.builder()
                .operationId(1L)
                .accountId(1L)
                .operationAmount(100.0)
                .date(LocalDateTime.of(2024, 2, 25, 9, 10, 20))
                .operationType(OperationType.DEPOSIT)
                .build();
        when(operationService.findAllOperationsByAccountId(anyLong())).thenReturn(
                Collections.singletonList(operation));

        mockMvc.perform(get(Api.OperationsApi.operationApi + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].operationId").value(operation.getOperationId()))
                .andExpect(jsonPath("$[0].operationAmount").value(operation.getOperationAmount()))
                .andExpect(jsonPath("$[0].accountId").value(operation.getAccountId()))
                .andExpect(jsonPath("$[0].operationType").value(operation.getOperationType().name()));
    }

    private String anyJsonOperation() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        Operation operation = Operation.builder()
                .operationId(1L)
                .accountId(1L)
                .operationAmount(100.0)
                .operationType(OperationType.DEPOSIT)
                .build();

        return objectMapper.writeValueAsString(operation);
    }
}