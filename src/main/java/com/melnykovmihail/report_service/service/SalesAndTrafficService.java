package com.melnykovmihail.report_service.service;

import com.melnykovmihail.report_service.data.dto.SalesAndTrafficByAsinDTO;
import com.melnykovmihail.report_service.data.dto.SalesAndTrafficByDateDTO;
import com.melnykovmihail.report_service.data.entity.SalesAndTrafficByAsin;
import com.melnykovmihail.report_service.data.entity.SalesAndTrafficByDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface SalesAndTrafficService {

    void upsertSalesAndTrafficByDate(SalesAndTrafficByDate salesAndTrafficByDate);

    void upsertSalesAndTrafficByAsin(SalesAndTrafficByAsin salesAndTrafficByAsin);

    SalesAndTrafficByDate saveSalesAndTrafficByDate(SalesAndTrafficByDate salesAndTrafficByDate);

    SalesAndTrafficByAsin saveSalesAndTrafficByAsin(SalesAndTrafficByAsin salesAndTrafficByAsin);

    Optional<SalesAndTrafficByDate> updateSalesAndTrafficByDate(String id, SalesAndTrafficByDate salesAndTrafficByDate);

    Optional<SalesAndTrafficByAsin> updateSalesAndTrafficByAsin(String id, SalesAndTrafficByAsin salesAndTrafficByAsin);

    void deleteSalesAndTrafficByDate(String id);

    void deleteSalesAndTrafficByAsin(String id);

    Page<SalesAndTrafficByDate> findAllDateReports(Pageable pageable);

    Optional<SalesAndTrafficByDate> findDateReportById(String id);

    Page<SalesAndTrafficByAsin> findAllAsinReports(Pageable pageable);

    Optional<SalesAndTrafficByAsin> findAsinReportById(String id);

    List<SalesAndTrafficByDate> findDateReportsInRange(Date startDate, Date endDate);

    List<SalesAndTrafficByAsin> findReportsByAsinList (List<String> asins);

    SalesAndTrafficByDateDTO findDateReportsSumInRange(Date startDate, Date endDate);

    SalesAndTrafficByAsinDTO findAsinReportsSumInList(List<String> asins);
}
