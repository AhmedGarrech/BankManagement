package com.garrech.bankmanagement.entities;

import com.garrech.bankmanagement.utils.OperationType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class Operation {

    private Long operationId;
    private Long accountId;
    private Double operationAmount;
    private OperationType operationType;
    private LocalDateTime date;
}