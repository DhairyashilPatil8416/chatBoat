package com.example.devopschatbot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
class DevopsChatbotApplicationTests {

    // Mock RestTemplate so no real HTTP calls are made to Ollama during tests
    @MockBean
    RestTemplate restTemplate;

    @Test
    void contextLoads() {
    }
}
