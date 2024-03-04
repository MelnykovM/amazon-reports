package com.melnykovmihail.report_service.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.melnykovmihail.report_service.data.entity.SalesAndTrafficByAsin;
import com.melnykovmihail.report_service.data.entity.SalesAndTrafficByDate;
import com.melnykovmihail.report_service.service.SalesAndTrafficService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Log4j
public class DatabaseUpdateTask {

    private final SalesAndTrafficService salesAndTrafficService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Scheduled(initialDelay = 0, fixedRate = 5000)
    public void updateDatabase() {

        String originalFilePath = "E:/amazon/src/main/resources/static/test_report.json";
        String copyFilePath = "E:/amazon/src/main/resources/static/test_report_copy.json";

        File originalFile = new File(originalFilePath);
        File copyFile = new File(copyFilePath);

        try {
            if (!copyFile.exists()) {
                log.info("Init DB");
                copyFile(originalFile, copyFile);
                processFile(copyFile);
            } else {
                if (!filesAreEqual(originalFile, copyFile)) {
                    log.info("Update DB");
                    copyFile(originalFile, copyFile);
                    processFile(copyFile);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void copyFile(File source, File dest) throws IOException {
        try (FileChannel sourceChannel = new FileInputStream(source).getChannel();
             FileChannel destChannel = new FileOutputStream(dest).getChannel()) {
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        }
    }

    private boolean filesAreEqual(File first, File second) throws IOException {
        byte[] firstFileContent = Files.readAllBytes(first.toPath());
        byte[] secondFileContent = Files.readAllBytes(second.toPath());
        return java.util.Arrays.equals(firstFileContent, secondFileContent);
    }

    public void processFile(File file) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        var start = LocalDateTime.now();
        try (JsonParser parser = new JsonFactory().createParser(file)) {
            while (!parser.isClosed()) {
                JsonToken jsonToken = parser.nextToken();

                if (JsonToken.FIELD_NAME.equals(jsonToken)) {
                    String fieldName = parser.getCurrentName();

                    parser.nextToken();

                    if ("reportSpecification".equals(fieldName)) {
                        JsonNode node = objectMapper.readTree(parser);
                        log.info("Info for DB: " + node);
                    } else if ("salesAndTrafficByDate".equals(fieldName)) {
                        while (parser.nextToken() != JsonToken.END_ARRAY) {
                            JsonNode node = objectMapper.readTree(parser);
                            executorService.submit(() -> {
                                try {
                                    SalesAndTrafficByDate salesAndTrafficByDate = objectMapper.treeToValue(node, SalesAndTrafficByDate.class);
                                    salesAndTrafficService.upsertSalesAndTrafficByDate(salesAndTrafficByDate);
                                } catch (Exception e) {
                                    log.error(e.getMessage());
                                }
                            });
                        }
                    } else if ("salesAndTrafficByAsin".equals(fieldName)) {
                        while (parser.nextToken() != JsonToken.END_ARRAY) {
                            JsonNode node = objectMapper.readTree(parser);
                            executorService.submit(() -> {
                                try {
                                    SalesAndTrafficByAsin salesAndTrafficByAsin = objectMapper.treeToValue(node, SalesAndTrafficByAsin.class);
                                    salesAndTrafficService.upsertSalesAndTrafficByAsin(salesAndTrafficByAsin);
                                } catch (Exception e) {
                                    log.error(e.getMessage());
                                }
                            });
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    log.error("executorService awaitTermination");
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                log.error(e.getMessage());
                executorService.shutdownNow();
            }
        }
        var end = LocalDateTime.now();
        Duration duration = Duration.between(start, end);
        log.info("DB update completed in " + duration.getNano() + " nano");
        log.info("DB update completed in " + duration.getSeconds() + " sec");
    }
}
