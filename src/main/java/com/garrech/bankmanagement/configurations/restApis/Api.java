package com.garrech.bankmanagement.configurations.restApis;

public interface Api {
    String baseApi = "/api";

    interface ClientsApi {
        String clientsApi = baseApi + "/clients";
        String searchClientsApi = clientsApi + "/search";
    }

    interface JobsApi {
        String jobApi = baseApi + "/startJob";
    }

    interface OperationsApi {
        String operationApi = baseApi + "/operation";
    }
}