package com.garrech.bankmanagement.services;

import com.garrech.bankmanagement.entities.Account;
import com.garrech.bankmanagement.entities.Operation;
import com.garrech.bankmanagement.utils.OperationType;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class OperationTransactionServiceTest {

    @Mock
    private AccountService accountService;
    @Mock
    private OperationService operationService;

    @InjectMocks
    private OperationTransactionService operationTransactionService;

    @Test
    public void transact_OperationAmountLessThan0_ReturnsFailed() {

        Operation operation = Operation.builder()
                .operationAmount(-100.0)
                .build();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            operationTransactionService.transact(operation);
        });
        assertEquals("Operation Refused : Amount must be greater than 0", exception.getMessage());
    }

    @Test
    public void transact_AccountAmountLessThanOperationAmount_ReturnsFailed() {
        Operation operation = Operation.builder()
                .operationAmount(100.0)
                .operationType(OperationType.WITHDRAWAL)
                .accountId(1L)
                .build();
        Account account = Account.builder()
                .accountId(1L)
                .accountAmount(50.0)
                .build();

        when(accountService.findAccountById(operation.getAccountId())).thenReturn(account);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            operationTransactionService.transact(operation);
        });
        assertEquals("Operation Refused: Insufficient funds", exception.getMessage());
    }

    @Test
    public void transact_ReturnsSuccess() {
        Operation operation = Operation.builder()
                .operationAmount(100.0)
                .operationType(OperationType.DEPOSIT)
                .accountId(1L)
                .build();
        Account account = Account.builder()
                .accountId(1L)
                .accountAmount(50.0)
                .build();

        when(accountService.findAccountById(operation.getAccountId())).thenReturn(account);

        operationTransactionService.transact(operation);

        assertEquals(account.getAccountAmount(), 150);
    }
}