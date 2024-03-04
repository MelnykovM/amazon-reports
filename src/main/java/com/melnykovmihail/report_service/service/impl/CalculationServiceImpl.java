package com.melnykovmihail.report_service.service.impl;

import com.melnykovmihail.report_service.data.dto.SalesAndTrafficByAsinDTO;
import com.melnykovmihail.report_service.data.dto.SalesAndTrafficByDateDTO;
import com.melnykovmihail.report_service.data.entity.*;
import com.melnykovmihail.report_service.service.CalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.atomic.LongAdder;

@Service
@RequiredArgsConstructor
public class CalculationServiceImpl implements CalculationService {

    public SalesAndTrafficByDateDTO calculateSumByDate(List<SalesAndTrafficByDate> salesAndTrafficByDateReports) {
        int reportsCount = salesAndTrafficByDateReports.size();

        List<SalesData> salesDatas = salesAndTrafficByDateReports.stream().map(SalesAndTrafficByDate::getSalesByDate).toList();
        List<TrafficData> trafficDatas = salesAndTrafficByDateReports.stream().map(SalesAndTrafficByDate::getTrafficByDate).toList();

        SalesAndTrafficByDateDTO aggregatedData = new SalesAndTrafficByDateDTO();
        aggregatedData.setReportsCount(reportsCount);
        aggregatedData.setSalesByDate(calculateSalesDataSum(salesDatas, reportsCount));
        aggregatedData.setTrafficByDate(calculateTrafficDataSum(trafficDatas, reportsCount));
        return aggregatedData;
    }

    public SalesAndTrafficByAsinDTO calculateSumByAsin(List<SalesAndTrafficByAsin> salesAndTrafficByAsinReports) {
        int reportsCount = salesAndTrafficByAsinReports.size();

        List<SalesData> salesDatas = salesAndTrafficByAsinReports.stream().map(SalesAndTrafficByAsin::getSalesByAsin).toList();
        List<TrafficData> trafficDatas = salesAndTrafficByAsinReports.stream().map(SalesAndTrafficByAsin::getTrafficByAsin).toList();

        SalesAndTrafficByAsinDTO aggregatedData = new SalesAndTrafficByAsinDTO();
        aggregatedData.setReportsCount(reportsCount);
        aggregatedData.setSalesByAsin(calculateSalesDataSum(salesDatas, reportsCount));
        aggregatedData.setTrafficByAsin(calculateTrafficDataSum(trafficDatas, reportsCount));
        return aggregatedData;
    }

    private TrafficData calculateTrafficDataSum(List<TrafficData> trafficDatas, int reportsCount) {
        LongAdder browserPageViews = new LongAdder();
        LongAdder browserPageViewsB2B = new LongAdder();
        LongAdder mobileAppPageViews = new LongAdder();
        LongAdder mobileAppPageViewsB2B = new LongAdder();
        LongAdder pageViews = new LongAdder();
        LongAdder pageViewsB2B = new LongAdder();
        LongAdder browserSessions = new LongAdder();
        LongAdder browserSessionsB2B = new LongAdder();
        LongAdder mobileAppSessions = new LongAdder();
        LongAdder mobileAppSessionsB2B = new LongAdder();
        LongAdder sessions = new LongAdder();
        LongAdder sessionsB2B = new LongAdder();
        DoubleAdder buyBoxPercentage = new DoubleAdder();
        DoubleAdder buyBoxPercentageB2B = new DoubleAdder();
        DoubleAdder orderItemSessionPercentage = new DoubleAdder();
        DoubleAdder orderItemSessionPercentageB2B = new DoubleAdder();
        DoubleAdder unitSessionPercentage = new DoubleAdder();
        DoubleAdder unitSessionPercentageB2B = new DoubleAdder();
        LongAdder averageOfferCount = new LongAdder();
        LongAdder averageParentItems = new LongAdder();
        LongAdder feedbackReceived = new LongAdder();
        LongAdder negativeFeedbackReceived = new LongAdder();
        DoubleAdder receivedNegativeFeedbackRate = new DoubleAdder();
        DoubleAdder browserSessionPercentage = new DoubleAdder();
        DoubleAdder browserSessionPercentageB2B = new DoubleAdder();
        DoubleAdder mobileAppSessionPercentage = new DoubleAdder();
        DoubleAdder mobileAppSessionPercentageB2B = new DoubleAdder();
        DoubleAdder sessionPercentage = new DoubleAdder();
        DoubleAdder sessionPercentageB2B = new DoubleAdder();
        DoubleAdder browserPageViewsPercentage = new DoubleAdder();
        DoubleAdder browserPageViewsPercentageB2B = new DoubleAdder();
        DoubleAdder mobileAppPageViewsPercentage = new DoubleAdder();
        DoubleAdder mobileAppPageViewsPercentageB2B = new DoubleAdder();
        DoubleAdder pageViewsPercentage = new DoubleAdder();
        DoubleAdder pageViewsPercentageB2B = new DoubleAdder();

        trafficDatas.parallelStream().forEach(trafficData -> {
            browserPageViews.add(trafficData.getBrowserPageViews());
            browserPageViewsB2B.add(trafficData.getBrowserPageViewsB2B());
            mobileAppPageViews.add(trafficData.getMobileAppPageViews());
            mobileAppPageViewsB2B.add(trafficData.getMobileAppPageViewsB2B());
            pageViews.add(trafficData.getPageViews());
            pageViewsB2B.add(trafficData.getPageViewsB2B());
            browserSessions.add(trafficData.getBrowserSessions());
            browserSessionsB2B.add(trafficData.getBrowserSessionsB2B());
            mobileAppSessions.add(trafficData.getMobileAppSessions());
            mobileAppSessionsB2B.add(trafficData.getMobileAppSessionsB2B());
            sessions.add(trafficData.getSessions());
            sessionsB2B.add(trafficData.getSessionsB2B());

            buyBoxPercentage.add(trafficData.getBuyBoxPercentage());
            buyBoxPercentageB2B.add(trafficData.getBuyBoxPercentageB2B());
            orderItemSessionPercentage.add(trafficData.getOrderItemSessionPercentage());
            orderItemSessionPercentageB2B.add(trafficData.getOrderItemSessionPercentageB2B());
            unitSessionPercentage.add(trafficData.getUnitSessionPercentage());
            unitSessionPercentageB2B.add(trafficData.getUnitSessionPercentageB2B());
            receivedNegativeFeedbackRate.add(trafficData.getReceivedNegativeFeedbackRate());
            browserSessionPercentage.add(trafficData.getBrowserSessionPercentage());
            browserSessionPercentageB2B.add(trafficData.getBrowserSessionPercentageB2B());
            mobileAppSessionPercentage.add(trafficData.getMobileAppSessionPercentage());
            mobileAppSessionPercentageB2B.add(trafficData.getMobileAppSessionPercentageB2B());
            sessionPercentage.add(trafficData.getSessionPercentage());
            sessionPercentageB2B.add(trafficData.getSessionPercentageB2B());
            browserPageViewsPercentage.add(trafficData.getBrowserPageViewsPercentage());
            browserPageViewsPercentageB2B.add(trafficData.getBrowserPageViewsPercentageB2B());
            mobileAppPageViewsPercentage.add(trafficData.getMobileAppPageViewsPercentage());
            mobileAppPageViewsPercentageB2B.add(trafficData.getMobileAppPageViewsPercentageB2B());
            pageViewsPercentage.add(trafficData.getPageViewsPercentage());
            pageViewsPercentageB2B.add(trafficData.getPageViewsPercentageB2B());

            averageOfferCount.add(trafficData.getAverageOfferCount());
            averageParentItems.add(trafficData.getAverageParentItems());
            feedbackReceived.add(trafficData.getFeedbackReceived());
            negativeFeedbackReceived.add(trafficData.getNegativeFeedbackReceived());
        });

        TrafficData trafficData = new TrafficData();

        trafficData.setBrowserPageViews(browserPageViews.intValue());
        trafficData.setBrowserPageViewsB2B(browserPageViewsB2B.intValue());
        trafficData.setMobileAppPageViews(mobileAppPageViews.intValue());
        trafficData.setMobileAppPageViewsB2B(mobileAppPageViewsB2B.intValue());
        trafficData.setPageViews(pageViews.intValue());
        trafficData.setPageViewsB2B(pageViewsB2B.intValue());
        trafficData.setBrowserSessions(browserSessions.intValue());
        trafficData.setBrowserSessionsB2B(browserSessionsB2B.intValue());
        trafficData.setMobileAppSessions(mobileAppSessions.intValue());
        trafficData.setMobileAppSessionsB2B(mobileAppSessionsB2B.intValue());
        trafficData.setSessions(sessions.intValue());
        trafficData.setSessionsB2B(sessionsB2B.intValue());

        trafficData.setBuyBoxPercentage(calculateResultPercentage(buyBoxPercentage.sum(), reportsCount));
        trafficData.setBuyBoxPercentageB2B(calculateResultPercentage(buyBoxPercentageB2B.sum(), reportsCount));
        trafficData.setOrderItemSessionPercentage(calculateResultPercentage(orderItemSessionPercentage.sum(), reportsCount));
        trafficData.setOrderItemSessionPercentageB2B(calculateResultPercentage(orderItemSessionPercentageB2B.sum(), reportsCount));
        trafficData.setUnitSessionPercentage(calculateResultPercentage(unitSessionPercentage.sum(), reportsCount));
        trafficData.setUnitSessionPercentageB2B(calculateResultPercentage(unitSessionPercentageB2B.sum(), reportsCount));
        trafficData.setBrowserSessionPercentage(calculateResultPercentage(browserSessionPercentage.sum(), reportsCount));
        trafficData.setBrowserSessionPercentageB2B(calculateResultPercentage(browserSessionPercentageB2B.sum(), reportsCount));
        trafficData.setMobileAppSessionPercentage(calculateResultPercentage(mobileAppSessionPercentage.sum(), reportsCount));
        trafficData.setMobileAppSessionPercentageB2B(calculateResultPercentage(mobileAppSessionPercentageB2B.sum(), reportsCount));
        trafficData.setSessionPercentage(calculateResultPercentage(sessionPercentage.sum(), reportsCount));
        trafficData.setSessionPercentageB2B(calculateResultPercentage(sessionPercentageB2B.sum(), reportsCount));
        trafficData.setBrowserPageViewsPercentage(calculateResultPercentage(browserPageViewsPercentage.sum(), reportsCount));
        trafficData.setBrowserPageViewsPercentageB2B(calculateResultPercentage(browserPageViewsPercentageB2B.sum(), reportsCount));
        trafficData.setMobileAppPageViewsPercentage(calculateResultPercentage(mobileAppPageViewsPercentage.sum(), reportsCount));
        trafficData.setMobileAppPageViewsPercentageB2B(calculateResultPercentage(mobileAppPageViewsPercentageB2B.sum(), reportsCount));
        trafficData.setPageViewsPercentage(calculateResultPercentage(pageViewsPercentage.sum(), reportsCount));
        trafficData.setPageViewsPercentageB2B(calculateResultPercentage(pageViewsPercentageB2B.sum(), reportsCount));
        trafficData.setAverageOfferCount(averageOfferCount.intValue());
        trafficData.setAverageParentItems(averageParentItems.intValue());
        trafficData.setFeedbackReceived(feedbackReceived.intValue());
        trafficData.setNegativeFeedbackReceived(negativeFeedbackReceived.intValue());
        trafficData.setReceivedNegativeFeedbackRate(receivedNegativeFeedbackRate.sum() / reportsCount);

        return trafficData;
    }

    private SalesData calculateSalesDataSum(List<SalesData> salesDatas, int reportsCount) {
        DoubleAdder orderedProductSalesAmount = new DoubleAdder();
        DoubleAdder orderedProductSalesB2BAmount = new DoubleAdder();
        LongAdder unitsOrdered = new LongAdder();
        LongAdder unitsOrderedB2B = new LongAdder();
        LongAdder totalOrderItems = new LongAdder();
        LongAdder totalOrderItemsB2B = new LongAdder();
        DoubleAdder averageSalesPerOrderItemAmount = new DoubleAdder();
        DoubleAdder averageSalesPerOrderItemB2BAmount = new DoubleAdder();
        DoubleAdder averageUnitsPerOrderItem = new DoubleAdder();
        DoubleAdder averageUnitsPerOrderItemB2B = new DoubleAdder();
        DoubleAdder averageSellingPriceAmount = new DoubleAdder();
        DoubleAdder averageSellingPriceB2BAmount = new DoubleAdder();
        LongAdder unitsRefunded = new LongAdder();
        DoubleAdder refundRate = new DoubleAdder();
        DoubleAdder claimsGranted = new DoubleAdder();
        DoubleAdder claimsAmountAmount = new DoubleAdder();
        DoubleAdder shippedProductSalesAmount = new DoubleAdder();
        LongAdder unitsShipped = new LongAdder();
        LongAdder ordersShipped = new LongAdder();

        salesDatas.parallelStream().forEach(salesData -> {

            unitsOrdered.add(salesData.getUnitsOrdered());
            unitsOrderedB2B.add(salesData.getUnitsOrderedB2B());
            totalOrderItems.add(salesData.getTotalOrderItems());
            totalOrderItemsB2B.add(salesData.getTotalOrderItemsB2B());
            averageUnitsPerOrderItem.add(salesData.getAverageUnitsPerOrderItem());
            averageUnitsPerOrderItemB2B.add(salesData.getAverageUnitsPerOrderItemB2B());
            unitsRefunded.add(salesData.getUnitsRefunded());
            refundRate.add(salesData.getRefundRate());
            claimsGranted.add(salesData.getClaimsGranted());
            unitsShipped.add(salesData.getUnitsShipped());
            ordersShipped.add(salesData.getOrdersShipped());

            orderedProductSalesAmount.add(salesData.getOrderedProductSales() != null ? salesData.getOrderedProductSales().getAmount() : 0);
            orderedProductSalesB2BAmount.add(salesData.getOrderedProductSalesB2B() != null ? salesData.getOrderedProductSalesB2B().getAmount() : 0);
            claimsAmountAmount.add(salesData.getClaimsAmount() != null ? salesData.getClaimsAmount().getAmount() : 0);
            shippedProductSalesAmount.add(salesData.getShippedProductSales() != null ? salesData.getShippedProductSales().getAmount() : 0);
            averageSalesPerOrderItemAmount.add(salesData.getAverageSalesPerOrderItem() != null ? salesData.getAverageSalesPerOrderItem().getAmount() : 0);
            averageSalesPerOrderItemB2BAmount.add(salesData.getAverageSalesPerOrderItemB2B() != null ? salesData.getAverageSalesPerOrderItemB2B().getAmount() : 0);
            averageSellingPriceAmount.add(salesData.getAverageSellingPrice() != null ? salesData.getAverageSellingPrice().getAmount() : 0);
            averageSellingPriceB2BAmount.add(salesData.getAverageSellingPriceB2B() != null ? salesData.getAverageSellingPriceB2B().getAmount() : 0);
        });

        SalesData salesData = new SalesData();

        salesData.setUnitsOrdered(unitsOrdered.intValue());
        salesData.setAverageUnitsPerOrderItem(calculateResultPercentage(averageUnitsPerOrderItem.sum(), reportsCount));
        salesData.setOrderedProductSales(createMoneyAmount(orderedProductSalesAmount.sum(), "USD"));
        salesData.setOrderedProductSalesB2B(createMoneyAmount(orderedProductSalesB2BAmount.sum(), "USD"));
        salesData.setUnitsOrderedB2B(unitsOrderedB2B.intValue());
        salesData.setTotalOrderItems(totalOrderItems.intValue());
        salesData.setTotalOrderItemsB2B(totalOrderItemsB2B.intValue());
        salesData.setAverageSalesPerOrderItem(createMoneyAmount(averageSalesPerOrderItemAmount.sum(), "USD"));
        salesData.setAverageSalesPerOrderItemB2B(createMoneyAmount(averageSalesPerOrderItemB2BAmount.sum(), "USD"));
        salesData.setAverageUnitsPerOrderItemB2B(calculateResultPercentage(averageUnitsPerOrderItemB2B.sum(), reportsCount));
        salesData.setAverageSellingPrice(createMoneyAmount(averageSellingPriceAmount.sum(), "USD"));
        salesData.setAverageSellingPriceB2B(createMoneyAmount(averageSellingPriceB2BAmount.sum(), "USD"));
        salesData.setUnitsRefunded(unitsRefunded.intValue());
        salesData.setRefundRate(calculateResultPercentage(refundRate.sum(), reportsCount));
        salesData.setClaimsGranted(calculateResultPercentage(claimsGranted.sum(), reportsCount));
        salesData.setClaimsAmount(createMoneyAmount(claimsAmountAmount.sum(), "USD"));
        salesData.setShippedProductSales(createMoneyAmount(shippedProductSalesAmount.sum(), "USD"));
        salesData.setUnitsShipped(unitsShipped.intValue());
        salesData.setOrdersShipped(ordersShipped.intValue());

        return salesData;
    }

    private MoneyAmount createMoneyAmount(double sum, String currency) {
        BigDecimal bigSum = BigDecimal.valueOf(sum);
        BigDecimal roundedSum = bigSum.setScale(2, RoundingMode.HALF_UP);
        return new MoneyAmount(roundedSum.doubleValue(), currency);
    }

    private double calculateResultPercentage(double sum, int count) {
        BigDecimal bigSum = BigDecimal.valueOf(sum);
        BigDecimal countB = BigDecimal.valueOf(count);
        BigDecimal average = bigSum.divide(countB, new MathContext(4, RoundingMode.HALF_UP));
        return average.doubleValue();
    }
}
