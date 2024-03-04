package com.melnykovmihail.report_service.data.entity;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesData {
    private MoneyAmount orderedProductSales;
    private MoneyAmount orderedProductSalesB2B;
    private int unitsOrdered;
    private int unitsOrderedB2B;
    private int totalOrderItems;
    private int totalOrderItemsB2B;
    private MoneyAmount averageSalesPerOrderItem;
    private MoneyAmount averageSalesPerOrderItemB2B;
    private double averageUnitsPerOrderItem;
    private double averageUnitsPerOrderItemB2B;
    private MoneyAmount averageSellingPrice;
    private MoneyAmount averageSellingPriceB2B;
    private int unitsRefunded;
    private double refundRate;
    private double claimsGranted;
    private MoneyAmount claimsAmount;
    private MoneyAmount shippedProductSales;
    private int unitsShipped;
    private int ordersShipped;
}
