package com.garrech.bankmanagement.services;

import com.garrech.bankmanagement.Repositories.OperationRepository;
import com.garrech.bankmanagement.entities.Operation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OperationServiceImpl implements OperationService {

    private final OperationRepository operationRepository;

    @Override
    public void save(Operation operation) {
        operationRepository.save(operation);
    }

    @Override
    public List<Operation> findAllOperationsByAccountId(Long accountId) {
        return operationRepository.findAllOperationsByAccountId(accountId);
    }
}
