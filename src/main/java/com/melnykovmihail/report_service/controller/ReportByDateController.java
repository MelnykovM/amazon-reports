package com.melnykovmihail.report_service.controller;

import com.melnykovmihail.report_service.data.dto.SalesAndTrafficByDateDTO;
import com.melnykovmihail.report_service.data.entity.SalesAndTrafficByDate;
import com.melnykovmihail.report_service.service.SalesAndTrafficService;
import com.melnykovmihail.report_service.validator.ReportValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/date-report")
@RequiredArgsConstructor
@Tag(name = "Date-report")
public class ReportByDateController {
    private final SalesAndTrafficService salesAndTrafficService;
    private final ReportValidator reportValidator;

    @Operation(summary = "Get all reports by Date")
    @GetMapping
    public ResponseEntity<Page<SalesAndTrafficByDate>> getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SalesAndTrafficByDate> salesAndTrafficByDatePage = salesAndTrafficService.findAllDateReports(pageable);
        return ResponseEntity.ok(salesAndTrafficByDatePage);
    }

    @Operation(summary = "Get report by Date with id")
    @GetMapping("/{id}")
    public ResponseEntity<SalesAndTrafficByDate> getById(@PathVariable String id) {
        Optional<SalesAndTrafficByDate> salesAndTrafficByDate = salesAndTrafficService.findDateReportById(id);
        return salesAndTrafficByDate.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create new report by Date")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody SalesAndTrafficByDate salesAndTrafficByDate) {
        if (reportValidator.validateSalesAndTrafficByDateForCreation(salesAndTrafficByDate)){
            return ResponseEntity.ok(salesAndTrafficService.saveSalesAndTrafficByDate(salesAndTrafficByDate));
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Report with this date already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @Operation(summary = "Update report by Date with id")
    @PutMapping("/{id}")
    public ResponseEntity<SalesAndTrafficByDate> update(@PathVariable String id, @RequestBody SalesAndTrafficByDate salesAndTrafficByDate) {
        return salesAndTrafficService.updateSalesAndTrafficByDate(id, salesAndTrafficByDate)
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete report by Date with id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (reportValidator.validateSalesAndTrafficByDateForDelete(id)){
            salesAndTrafficService.deleteSalesAndTrafficByDate(id);
            return ResponseEntity.<Void>ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get all reports by Date in range")
    @GetMapping("/range")
    public ResponseEntity<List<SalesAndTrafficByDate>> getByDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        List<SalesAndTrafficByDate> salesAndTrafficByDateList = salesAndTrafficService.findDateReportsInRange(startDate, endDate);
        if (salesAndTrafficByDateList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(salesAndTrafficByDateList);
        }
    }

    @Operation(summary = "Get summary by Date in range")
    @GetMapping("/sum-range")
    public ResponseEntity<SalesAndTrafficByDateDTO> getSumByDateRange(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        SalesAndTrafficByDateDTO salesAndTrafficByDateSum = salesAndTrafficService.findDateReportsSumInRange(startDate, endDate);
        return ResponseEntity.ok(salesAndTrafficByDateSum);
    }
}
