package com.garrech.bankmanagement.services;

import com.garrech.bankmanagement.entities.Operation;

import java.util.List;

public interface OperationService {
    void save(Operation operation);

    public List<Operation> findAllOperationsByAccountId(Long accountId);
}
