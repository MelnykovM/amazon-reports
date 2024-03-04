package com.melnykovmihail.report_service.data.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesAndTrafficByDate {
    @Id
    private String id;
    private Date date;
    @JsonProperty("salesByDate")
    private SalesData salesByDate;
    @JsonProperty("trafficByDate")
    private TrafficData trafficByDate;
}
