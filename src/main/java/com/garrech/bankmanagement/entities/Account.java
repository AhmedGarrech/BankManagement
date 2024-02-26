package com.garrech.bankmanagement.entities;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Account {
    private Long accountId;
    private Double accountAmount;
}
