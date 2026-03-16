package com.example.devopschatbot.ai;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OllamaClient {

    private final RestTemplate restTemplate;

    @Value("${ollama.base-url:http://localhost:11434}")
    private String baseUrl;

    @Value("${ollama.model:llama3.2}")
    private String model;

    public OllamaClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String generateReply(String prompt) {
        try {
            String url = baseUrl + "/api/generate";

            Map<String, Object> payload = new HashMap<>();
            payload.put("model", model);
            payload.put("prompt", prompt);
            payload.put("stream", false);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);

            @SuppressWarnings("unchecked")  
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);

            if (response != null && response.get("response") != null) {
                return response.get("response").toString().trim();
            }
            return "AI service responded with an empty message.";
        } catch (Exception ex) {
            return "AI service is unavailable right now. You can still use pipeline commands: build, deploy, status, rollback.";
        }  
    }
}
