package com.cloudLogging.service;

//import com.google.api.MonitoredResource;
import com.google.cloud.logging.Logging;
import com.google.cloud.logging.LoggingOptions;
import com.google.cloud.logging.LogEntry;
import com.google.cloud.MonitoredResource;
import com.google.cloud.logging.Payload.StringPayload;
import com.google.cloud.logging.Severity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class LoggingService {

    private static final Logger localLogger = LoggerFactory.getLogger(LoggingService.class);
    private final Logging cloudLogging;

    public LoggingService() {
        this.cloudLogging = LoggingOptions.newBuilder()
                .setProjectId("scam-botpoc")
                .build()
                .getService();

        System.out.println("Project ID: " + LoggingOptions.getDefaultProjectId());
    }

    public void log(String message, Severity severity) {

        MonitoredResource resource = MonitoredResource.newBuilder("global").build();
        // 1. Log locally
        logLocally(severity, message);

        // 2. Log to Google Cloud Logging
        try {
            LogEntry entry = LogEntry.newBuilder(StringPayload.of(message))
                    .setSeverity(Severity.INFO)
                    .setLogName("custom-app-log")
                    .setResource(resource)
                    .build();

            cloudLogging.write(Collections.singleton(entry));
            cloudLogging.flush();
        } catch (Exception e) {
            localLogger.error("Failed to write log to Google Cloud Logging: {}", e.getMessage());
        }
    }

    private void logLocally(Severity severity, String message) {
        switch (severity.name()) {
            case "DEBUG" -> localLogger.debug(message);
            case "INFO", "DEFAULT" -> localLogger.info(message);
            case "NOTICE", "WARNING" -> localLogger.warn(message);
            case "ERROR", "CRITICAL", "ALERT", "EMERGENCY" -> localLogger.error(message);
            default -> localLogger.info(message);
        }
    }

    // Convenience methods

    public void logInfo(String message) {
        log(message, Severity.INFO);
    }

    public void logWarn(String message) {
        log(message, Severity.WARNING);
    }

    public void logError(String message) {
        log(message, Severity.ERROR);
    }

    public void logDebug(String message) {
        log(message, Severity.DEBUG);
    }
}
