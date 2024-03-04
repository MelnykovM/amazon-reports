package com.melnykovmihail.report_service.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class KeyGenerator {

    public static String generateDateRangeKey(Date startDate, Date endDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        return dateFormat.format(startDate) + "-" + dateFormat.format(endDate);
    }

    public static String generateAsinListKey(List<String> asins) {
        return asins.stream()
                .sorted()
                .collect(Collectors.joining("-"));
    }
}
