# DevOps Chatbot (Spring Boot)

Small project using this flow:
- controller/ChatController.java
- service/ChatService.java
- service/PipelineService.java
- ai/OllamaClient.java
- config/AppConfig.java
- DevopsChatbotApplication.java

## Run locally

Requirements:
- Java 21
- Maven 3.9+
- Optional: Ollama running at http://localhost:11434

Run:

```bash
mvn spring-boot:run
```

## API

Health check:

```bash
curl http://localhost:8080/api/chat/health
```

Chat request:

```bash
curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d "{\"message\":\"deploy to staging\"}"
```

Sample commands:
- build
- deploy
- status
- rollback

## CI/CD

Pipeline file:
- .github/workflows/ci-cd.yml

What it does:
1. On push/PR to main and develop, run tests and package JAR.
2. Upload JAR as artifact.
3. On push to main, run sample deploy job.

## Git workflow

See GIT_WORKFLOW.md for complete branch and release flow.
