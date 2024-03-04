package com.melnykovmihail.report_service.data.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesAndTrafficByAsin {
    @Id
    private String id;
    private String parentAsin;
    @JsonProperty("salesByAsin")
    private SalesData salesByAsin;
    @JsonProperty("trafficByAsin")
    private TrafficData trafficByAsin;
}
