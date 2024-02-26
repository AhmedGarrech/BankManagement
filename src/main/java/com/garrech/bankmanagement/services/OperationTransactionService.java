package com.garrech.bankmanagement.services;

import com.garrech.bankmanagement.entities.Account;
import com.garrech.bankmanagement.entities.Operation;
import com.garrech.bankmanagement.utils.OperationType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class OperationTransactionService {

    private final OperationService operationService;
    private final AccountService accountService;

    public void transact(Operation operation) {
        saveOperation(operation);
        updateAccount(operation);
    }

    private void saveOperation(Operation operation) {
        if (operation.getOperationAmount() <= 0) {
            throw new IllegalArgumentException("Operation Refused : Amount must be greater than 0");
        }
        operation.setDate(LocalDateTime.now());
        operationService.save(operation);
    }

    private void updateAccount(Operation operation) {
        Account account = accountService.findAccountById(operation.getAccountId());
        Double newAmount = setCountAmount(operation.getOperationType(), account.getAccountAmount(), operation.getOperationAmount());
        account.setAccountAmount(newAmount);
        accountService.update(account);
    }

    private Double setCountAmount(OperationType operationType, Double currentAmount, Double operationAmount) {
        return switch (operationType) {
            case DEPOSIT -> deposit(currentAmount, operationAmount);
            case WITHDRAWAL -> withdraw(currentAmount, operationAmount);
            default -> throw new IllegalArgumentException("Invalid operation type");
        };
    }

    private static Double withdraw(Double currentAmount, Double operationAmount) {
        if (operationAmount <= currentAmount) {
            return currentAmount - operationAmount;
        } else {
            throw new IllegalArgumentException("Operation Refused: Insufficient funds");
        }
    }

    private static Double deposit(Double currentAmount, Double operationAmount) {
        return currentAmount + operationAmount;
    }
}
