package com.garrech.bankmanagement.controllers;

import com.garrech.bankmanagement.configurations.exceptions.CrudException;
import com.garrech.bankmanagement.configurations.restApis.Api;
import com.garrech.bankmanagement.entities.ClientInfo;
import com.garrech.bankmanagement.services.BatchService;
import com.garrech.bankmanagement.services.ClientService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping(Api.ClientsApi.clientsApi)
public class ClientController {

    private final ClientService clientService;
    private final BatchService batchService;

    @PostMapping()
    public ResponseEntity<String> save(@RequestBody String clientsFilePath) {
        try {
            batchService.runSaveClientBatchJob(clientsFilePath);
            return ResponseEntity.ok("Job Finnish successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Job Failed: " + e.getMessage());
        }
    }

    @GetMapping()
    public ResponseEntity<List<ClientInfo>> findAllClient() {
        try {
            return ResponseEntity.ok(clientService.findAllClients());
        } catch (Exception e) {
            throw new CrudException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<ClientInfo>> searchClients(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String clientType,
            @RequestParam(required = false) String accountId) {

        Map<String, String> searchCriteria = new HashMap<>();
        if (name != null) searchCriteria.put("name", name);
        if (clientType != null) searchCriteria.put("clientType", clientType);
        if (accountId != null) searchCriteria.put("accountId", accountId);

        try {
            return ResponseEntity.ok(clientService.searchClients(searchCriteria));
        } catch (Exception e) {
            throw new CrudException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
