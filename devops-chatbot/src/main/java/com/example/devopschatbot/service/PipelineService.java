package com.example.devopschatbot.service;

import org.springframework.stereotype.Service;

@Service
public class PipelineService {

    public String handleCommand(String command) {
        String normalized = command.toLowerCase();

        if (normalized.contains("build")) {
            return "Build pipeline started";
        }
        if (normalized.contains("deploy")) {
            return "Deployment started on AWS";
        }
        if (normalized.contains("status")) {
            return "Build successful and deployed";
        }
        if (normalized.contains("rollback")) {
            return "Pipeline: Rollback started to previous stable version.";
        }

        return "Pipeline: Command not recognized. Try build, deploy, status, or rollback.";
    }
}
