package com.cloudLogging.controller;

import com.cloudLogging.model.User;
import com.cloudLogging.service.LoggingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final LoggingService loggingService;

    public UserController(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    @PostMapping("/create")
    public String createUser(@RequestBody User user) {
        loggingService.logInfo("Creating user with ID: " + user.getId() + ", Name: " + user.getName());
        return "User " + user.getName() + " created successfully.";
    }

    @GetMapping("/simulate-warning")
    public String simulateWarning() {
        loggingService.logWarn("Simulated warning triggered.");
        return "Warning log sent.";
    }

    @GetMapping("/simulate-error")
    public String simulateError() {
        loggingService.logError("Simulated error triggered.");
        return "Error log sent.";
    }

    @GetMapping("/simulate-debug")
    public String simulateDebug() {
        loggingService.logDebug("Debug log for troubleshooting.");
        return "Debug log sent.";
    }
}

