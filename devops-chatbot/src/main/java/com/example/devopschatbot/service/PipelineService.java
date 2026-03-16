package com.example.devopschatbot.service;

import org.springframework.stereotype.Service;

@Service
public class PipelineService {

    private final GitHubActionsService gitHubActionsService;

    public PipelineService(GitHubActionsService gitHubActionsService) {
        this.gitHubActionsService = gitHubActionsService;
    }

    public String handleCommand(String command) {
        String normalized = command.toLowerCase();

        if (normalized.contains("build")) {
            return gitHubActionsService.triggerBuild();
        }
        if (normalized.contains("deploy")) {
            return gitHubActionsService.triggerDeploy();
        }
        if (normalized.contains("status")) {
            return gitHubActionsService.getLatestRunStatus();
        }
        if (normalized.contains("rollback")) {
            return "Pipeline: Rollback started to previous stable version.";
        }

        return "Pipeline: Command not recognized. Try build, deploy, status, or rollback.";
    }
}
