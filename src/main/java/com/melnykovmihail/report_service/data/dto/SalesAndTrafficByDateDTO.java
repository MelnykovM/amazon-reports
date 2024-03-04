package com.melnykovmihail.report_service.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.melnykovmihail.report_service.data.entity.SalesData;
import com.melnykovmihail.report_service.data.entity.TrafficData;
import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesAndTrafficByDateDTO {
    private Date startDate;
    private Date endDate;
    private int reportsCount;
    @JsonProperty("salesByDate")
    private SalesData salesByDate;
    @JsonProperty("trafficByDate")
    private TrafficData trafficByDate;
}
