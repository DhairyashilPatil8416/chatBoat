package com.example.devopschatbot.service;

import org.springframework.stereotype.Service;

import com.example.devopschatbot.ai.OllamaClient;

@Service
public class ChatService {

    private final PipelineService pipelineService;
    private final OllamaClient ollamaClient;

    public ChatService(PipelineService pipelineService, OllamaClient ollamaClient) {
        this.pipelineService = pipelineService;
        this.ollamaClient = ollamaClient;
    }

    public String processMessage(String message) {
        if (message == null || message.isBlank()) {
            return "Please send a DevOps question or command like build, deploy, or status.";
        }

        if (isPipelineCommand(message)) {
            return pipelineService.handleCommand(message);
        }

        return ollamaClient.generateReply(message);
    }

    public String getPipelineStatus() {
        return pipelineService.handleCommand("status");
    }

    private boolean isPipelineCommand(String message) {
        String m = message.toLowerCase();
        return m.contains("build") || m.contains("deploy") || m.contains("status") || m.contains("rollback");
    }
}
