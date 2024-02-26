package com.garrech.bankmanagement.controllers;

import com.garrech.bankmanagement.configurations.exceptions.CrudException;
import com.garrech.bankmanagement.configurations.restApis.Api;
import com.garrech.bankmanagement.entities.Operation;
import com.garrech.bankmanagement.services.OperationService;
import com.garrech.bankmanagement.services.OperationTransactionService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(Api.OperationsApi.operationApi)
public class OperationController {

    private final OperationService operationService;
    private final OperationTransactionService operationTransactionService;

    @PostMapping()
    public ResponseEntity<String> saveOperation(@RequestBody Operation operation) {
        try {
            operationTransactionService.transact(operation);
            return ResponseEntity.ok("Operation Saved");
        } catch (Exception e) {
            throw new CrudException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<List<Operation>> findOperationsByAccountId(@PathVariable Long accountId) {
        try {
            return ResponseEntity.ok(operationService.findAllOperationsByAccountId(accountId));
        } catch (Exception e) {
            throw new CrudException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
