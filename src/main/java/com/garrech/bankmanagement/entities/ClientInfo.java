package com.garrech.bankmanagement.entities;

import com.garrech.bankmanagement.utils.ClientType;
import com.garrech.bankmanagement.utils.OperationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class ClientInfo {
    private Long clientId;
    private String clientName;
    private ClientType clientType;
    private Long accountId;
    private Double accountAmount;
    private Long operationId;
    private Double operationAmount;
    private OperationType operationType;
    private LocalDateTime operationDate;
}
