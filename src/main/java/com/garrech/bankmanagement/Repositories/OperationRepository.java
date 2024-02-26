package com.garrech.bankmanagement.Repositories;

import com.garrech.bankmanagement.entities.Operation;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepository {
    void save(Operation operation);

    List<Operation> findAllOperationsByAccountId(Long accountId);
}
