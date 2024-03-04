package com.melnykovmihail.report_service.controller;

import com.melnykovmihail.report_service.data.dto.SalesAndTrafficByAsinDTO;
import com.melnykovmihail.report_service.data.entity.SalesAndTrafficByAsin;
import com.melnykovmihail.report_service.service.SalesAndTrafficService;
import com.melnykovmihail.report_service.validator.ReportValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/asin-report")
@RequiredArgsConstructor
@Tag(name = "Asin-report")
public class ReportByAsinController {
    private final SalesAndTrafficService salesAndTrafficService;
    private final ReportValidator reportValidator;

    @Operation(summary = "Get all reports by ASIN")
    @GetMapping
    public ResponseEntity<Page<SalesAndTrafficByAsin>> getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SalesAndTrafficByAsin> salesAndTrafficByAsinPage = salesAndTrafficService.findAllAsinReports(pageable);
        return ResponseEntity.ok(salesAndTrafficByAsinPage);
    }

    @Operation(summary = "Get report by ASIN with id")
    @GetMapping("/{id}")
    public ResponseEntity<SalesAndTrafficByAsin> getById(@PathVariable String id) {
        Optional<SalesAndTrafficByAsin> salesAndTrafficByAsin = salesAndTrafficService.findAsinReportById(id);
        return salesAndTrafficByAsin.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create new report by ASIN")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody SalesAndTrafficByAsin salesAndTrafficByAsin) {
        if (reportValidator.validateSalesAndTrafficByAsinForCreation(salesAndTrafficByAsin)) {
            return ResponseEntity.ok(salesAndTrafficService.saveSalesAndTrafficByAsin(salesAndTrafficByAsin));
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Report with this ASIN already exists.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    @Operation(summary = "Update report by ASIN with id")
    @PutMapping("/{id}")
    public ResponseEntity<SalesAndTrafficByAsin> update(@PathVariable String id, @RequestBody SalesAndTrafficByAsin salesAndTrafficByAsin) {
        return salesAndTrafficService.updateSalesAndTrafficByAsin(id, salesAndTrafficByAsin)
                .map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete report by ASIN with id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        if (reportValidator.validateSalesAndTrafficByAsinForDelete(id)) {
            salesAndTrafficService.deleteSalesAndTrafficByAsin(id);
            return ResponseEntity.<Void>ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get all reports by ASINs list")
    @GetMapping("/list")
    public ResponseEntity<List<SalesAndTrafficByAsin>> getByAsinList(@RequestParam("asins") List<String> asins) {
        List<SalesAndTrafficByAsin> reports = salesAndTrafficService.findReportsByAsinList(asins);
        if (reports.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(reports);
        }
    }

    @Operation(summary = "Get summary by ASINs list")
    @GetMapping("/sum-list")
    public ResponseEntity<SalesAndTrafficByAsinDTO> getSumByAsinList(@RequestParam("asins") List<String> asins) {
        SalesAndTrafficByAsinDTO salesAndTrafficByAsinSum = salesAndTrafficService.findAsinReportsSumInList(asins);
        return ResponseEntity.ok(salesAndTrafficByAsinSum);
    }
}
