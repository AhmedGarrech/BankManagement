package com.garrech.bankmanagement.entities;

import com.garrech.bankmanagement.utils.ClientType;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Client {
    private Long clientId;
    private String clientName;
    private String password;
    private ClientType clientType;
    private Long accountId;
}
