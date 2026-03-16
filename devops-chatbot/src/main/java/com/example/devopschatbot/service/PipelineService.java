package com.example.devopschatbot.service;

import org.springframework.stereotype.Service;

@Service
public class PipelineService {

    public String handleCommand(String command) {
        String normalized = command.toLowerCase();

        if (normalized.contains("build")) {
            return "Pipeline: Build triggered successfully.";
        }
        if (normalized.contains("deploy")) {
            return "Pipeline: Deployment started to staging environment.";
        }
        if (normalized.contains("status")) {
            return "Pipeline: Latest run is SUCCESS. No active failures.";
        }
        if (normalized.contains("rollback")) {
            return "Pipeline: Rollback started to previous stable version.";
        }

        return "Pipeline: Command not recognized. Try build, deploy, status, or rollback.";
    }
}
