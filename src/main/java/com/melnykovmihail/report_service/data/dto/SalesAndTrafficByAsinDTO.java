package com.melnykovmihail.report_service.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.melnykovmihail.report_service.data.entity.SalesData;
import com.melnykovmihail.report_service.data.entity.TrafficData;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesAndTrafficByAsinDTO {
    private List<String> asins;
    private int reportsCount;
    @JsonProperty("salesByAsin")
    private SalesData salesByAsin;
    @JsonProperty("trafficByAsin")
    private TrafficData trafficByAsin;
}
