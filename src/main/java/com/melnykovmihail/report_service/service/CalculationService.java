package com.melnykovmihail.report_service.service;

import com.melnykovmihail.report_service.data.dto.SalesAndTrafficByAsinDTO;
import com.melnykovmihail.report_service.data.dto.SalesAndTrafficByDateDTO;
import com.melnykovmihail.report_service.data.entity.SalesAndTrafficByAsin;
import com.melnykovmihail.report_service.data.entity.SalesAndTrafficByDate;

import java.util.List;

public interface CalculationService {

    SalesAndTrafficByDateDTO calculateSumByDate(List<SalesAndTrafficByDate> salesAndTrafficByDateReports);

    SalesAndTrafficByAsinDTO calculateSumByAsin(List<SalesAndTrafficByAsin> salesAndTrafficByAsinReports);
}
