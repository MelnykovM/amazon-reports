package com.melnykovmihail.report_service.data.entity;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoneyAmount {
    private double amount;
    private String currencyCode;
}
