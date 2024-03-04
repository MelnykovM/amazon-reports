package com.melnykovmihail.report_service.service.impl;

import com.melnykovmihail.report_service.data.dto.SalesAndTrafficByAsinDTO;
import com.melnykovmihail.report_service.data.dto.SalesAndTrafficByDateDTO;
import com.melnykovmihail.report_service.data.entity.SalesAndTrafficByAsin;
import com.melnykovmihail.report_service.data.entity.SalesAndTrafficByDate;
import com.melnykovmihail.report_service.data.repository.SalesAndTrafficByAsinRepository;
import com.melnykovmihail.report_service.data.repository.SalesAndTrafficByDateRepository;
import com.melnykovmihail.report_service.service.CalculationService;
import com.melnykovmihail.report_service.service.SalesAndTrafficService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalesAndTrafficServiceImpl implements SalesAndTrafficService {

    private final MongoTemplate mongoTemplate;
    private final SalesAndTrafficByDateRepository salesAndTrafficByDateRepository;
    private final SalesAndTrafficByAsinRepository salesAndTrafficByAsinRepository;
    private final CalculationService calculationService;

    @Override
    @Caching(evict = {
            @CacheEvict(value = "listSalesAndTrafficByDate", allEntries = true),
            @CacheEvict(value = "salesAndTrafficSumByDate", allEntries = true)})
    public void upsertSalesAndTrafficByDate(SalesAndTrafficByDate salesAndTrafficByDate) {
        Query query = new Query(Criteria.where("date").is(salesAndTrafficByDate.getDate()));
        Update update = new Update()
                .set("salesByDate", salesAndTrafficByDate.getSalesByDate())
                .set("trafficByDate", salesAndTrafficByDate.getTrafficByDate());
        mongoTemplate.upsert(query, update, SalesAndTrafficByDate.class);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "listSalesAndTrafficByAsin", allEntries = true),
            @CacheEvict(value = "salesAndTrafficSumByAsin", allEntries = true)})
    public void upsertSalesAndTrafficByAsin(SalesAndTrafficByAsin salesAndTrafficByAsin) {
        Query query = new Query(Criteria.where("parentAsin").is(salesAndTrafficByAsin.getParentAsin()));
        Update update = new Update()
                .set("salesByAsin", salesAndTrafficByAsin.getSalesByAsin())
                .set("trafficByAsin", salesAndTrafficByAsin.getTrafficByAsin());
        mongoTemplate.upsert(query, update, SalesAndTrafficByAsin.class);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "listSalesAndTrafficByDate", allEntries = true),
            @CacheEvict(value = "salesAndTrafficSumByDate", allEntries = true)})
    public SalesAndTrafficByDate saveSalesAndTrafficByDate(SalesAndTrafficByDate salesAndTrafficByDate) {
        return salesAndTrafficByDateRepository.save(salesAndTrafficByDate);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "listSalesAndTrafficByAsin", allEntries = true),
            @CacheEvict(value = "salesAndTrafficSumByAsin", allEntries = true)})
    public SalesAndTrafficByAsin saveSalesAndTrafficByAsin(SalesAndTrafficByAsin salesAndTrafficByAsin) {
        return salesAndTrafficByAsinRepository.save(salesAndTrafficByAsin);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "listSalesAndTrafficByDate", allEntries = true),
            @CacheEvict(value = "salesAndTrafficSumByDate", allEntries = true)})
    public Optional<SalesAndTrafficByDate> updateSalesAndTrafficByDate(String id, SalesAndTrafficByDate salesAndTrafficByDate) {
        return salesAndTrafficByDateRepository.findById(id)
                .map(existing -> {
                    salesAndTrafficByDate.setId(id);
                    return Optional.of(salesAndTrafficByDateRepository.save(salesAndTrafficByDate));
                }).orElse( Optional.empty());
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "listSalesAndTrafficByAsin", allEntries = true),
            @CacheEvict(value = "salesAndTrafficSumByAsin", allEntries = true)})
    public Optional<SalesAndTrafficByAsin> updateSalesAndTrafficByAsin(String id, SalesAndTrafficByAsin salesAndTrafficByAsin) {
        return salesAndTrafficByAsinRepository.findById(id)
                .map(existing -> {
                    salesAndTrafficByAsin.setId(id);
                    return Optional.of(salesAndTrafficByAsinRepository.save(salesAndTrafficByAsin));
                }).orElse( Optional.empty());
    }

    @Override
    public void deleteSalesAndTrafficByDate(String id) {
        salesAndTrafficByDateRepository.findById(id).ifPresent(this::deleteCacheDateReport);
        salesAndTrafficByDateRepository.deleteById(id);
    }

    @Override
    public void deleteSalesAndTrafficByAsin(String id) {
        salesAndTrafficByAsinRepository.findById(id).ifPresent(this::deleteCacheAsinReport);
        salesAndTrafficByAsinRepository.deleteById(id);
    }

    @Caching(evict = {
            @CacheEvict(value = "listSalesAndTrafficByDate", allEntries = true),
            @CacheEvict(value = "salesAndTrafficSumByDate", allEntries = true)})
    public void deleteCacheDateReport(SalesAndTrafficByDate salesAndTrafficByDate) {}

    @Caching(evict = {
            @CacheEvict(value = "listSalesAndTrafficByAsin", allEntries = true),
            @CacheEvict(value = "salesAndTrafficSumByAsin", allEntries = true)})
    public void deleteCacheAsinReport(SalesAndTrafficByAsin salesAndTrafficByAsin) {}

    @Override
    @Cacheable(value = "listSalesAndTrafficByDate")
    public Page<SalesAndTrafficByDate> findAllDateReports(Pageable pageable) {
        return salesAndTrafficByDateRepository.findAll(pageable);
    }

    @Override
    public Optional<SalesAndTrafficByDate> findDateReportById(String id) {
        return salesAndTrafficByDateRepository.findById(id);
    }

    @Override
    @Cacheable(value = "listSalesAndTrafficByAsin")
    public Page<SalesAndTrafficByAsin> findAllAsinReports(Pageable pageable) {
        return salesAndTrafficByAsinRepository.findAll(pageable);
    }

    @Override
    public Optional<SalesAndTrafficByAsin> findAsinReportById(String id) {
        return salesAndTrafficByAsinRepository.findById(id);
    }

    @Override
    @Cacheable(value = "listSalesAndTrafficByDate", key = "T(com.melnykovmihail.report_service.util.KeyGenerator).generateDateRangeKey(#startDate, #endDate)")
    public List<SalesAndTrafficByDate> findDateReportsInRange(Date startDate, Date endDate) {
        return salesAndTrafficByDateRepository.findByDateBetween(startDate, endDate);
    }

    @Override
    @Cacheable(value = "listSalesAndTrafficByAsin", key = "T(com.melnykovmihail.report_service.util.KeyGenerator).generateAsinListKey(#asins)")
    public List<SalesAndTrafficByAsin> findReportsByAsinList(List<String> asins) {
        return salesAndTrafficByAsinRepository.findByParentAsinIn(asins);
    }

    @Override
    @Cacheable(value = "salesAndTrafficSumByDate", key = "T(com.melnykovmihail.report_service.util.KeyGenerator).generateDateRangeKey(#startDate, #endDate)")
    public SalesAndTrafficByDateDTO findDateReportsSumInRange(Date startDate, Date endDate) {
        List<SalesAndTrafficByDate> salesAndTrafficByDateReports = salesAndTrafficByDateRepository.findByDateBetween(startDate, endDate);
        SalesAndTrafficByDateDTO salesAndTrafficByDateSum = calculationService.calculateSumByDate(salesAndTrafficByDateReports);
        salesAndTrafficByDateSum.setStartDate(startDate);
        salesAndTrafficByDateSum.setEndDate(endDate);
        return salesAndTrafficByDateSum;
    }

    @Override
    @Cacheable(value = "salesAndTrafficSumByAsin", key = "T(com.melnykovmihail.report_service.util.KeyGenerator).generateAsinListKey(#asins)")
    public SalesAndTrafficByAsinDTO findAsinReportsSumInList(List<String> asins) {
        List<SalesAndTrafficByAsin> salesAndTrafficByAsinReports = salesAndTrafficByAsinRepository.findByParentAsinIn(asins);
        SalesAndTrafficByAsinDTO salesAndTrafficByAsinSum = calculationService.calculateSumByAsin(salesAndTrafficByAsinReports);
        salesAndTrafficByAsinSum.setAsins(asins);
        return salesAndTrafficByAsinSum;
    }
}
