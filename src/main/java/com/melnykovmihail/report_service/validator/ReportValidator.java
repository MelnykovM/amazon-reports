package com.melnykovmihail.report_service.validator;

import com.melnykovmihail.report_service.data.entity.SalesAndTrafficByAsin;
import com.melnykovmihail.report_service.data.entity.SalesAndTrafficByDate;
import com.melnykovmihail.report_service.data.repository.SalesAndTrafficByAsinRepository;
import com.melnykovmihail.report_service.data.repository.SalesAndTrafficByDateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportValidator {

    private final SalesAndTrafficByDateRepository salesAndTrafficByDateRepository;
    private final SalesAndTrafficByAsinRepository salesAndTrafficByAsinRepository;

    public boolean validateSalesAndTrafficByDateForCreation(SalesAndTrafficByDate salesAndTrafficByDate) {
        return salesAndTrafficByDateRepository.findByDate(salesAndTrafficByDate.getDate()).isEmpty();
    }

    public boolean validateSalesAndTrafficByAsinForCreation(SalesAndTrafficByAsin salesAndTrafficByAsin) {
        return salesAndTrafficByAsinRepository.findByParentAsin(salesAndTrafficByAsin.getParentAsin()).isEmpty();
    }

    public boolean validateSalesAndTrafficByDateForDelete(String id) {
        return salesAndTrafficByDateRepository.findById(id).isPresent();
    }

    public boolean validateSalesAndTrafficByAsinForDelete(String id) {
        return salesAndTrafficByAsinRepository.findById(id).isPresent();
    }
}
