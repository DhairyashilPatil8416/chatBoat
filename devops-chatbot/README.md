# DevOps Chatbot (Simple Project)

This project is a simple Spring Boot chatbot with:
- Chat UI at `http://localhost:8080`
- DevOps commands: build, deploy, status, rollback
- GitHub Actions integration for real pipeline triggers
- Bot sound effects + voice (text-to-speech)

## 1) Quick Start

Requirements:
- Java 21
- Internet access (for GitHub API and optional Ollama)
- Optional: Ollama at `http://localhost:11434`

Run:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Open:
- `http://localhost:8080`

## 2) Simple Chat Commands

Use these exact messages in the chatbot:

- `build project`
  - Bot: `Build pipeline started`
- `deploy backend`
  - Bot: `Deployment started on AWS`
- `check pipeline status`
  - Bot: `Build successful and deployed` (or current live status)

## 3) Real GitHub Actions Setup

To make this a real project (not only demo responses), configure:

1. Create a GitHub Personal Access Token with:
   - `repo`
   - `workflow`
2. Set environment variable:

Windows PowerShell:

```powershell
$env:GITHUB_TOKEN="your_token_here"
```

3. Restart app.

The backend uses these values from `application.yml`:
- `github.owner`
- `github.repo`
- `github.workflow`
- `github.branch`

## 4) API Endpoints

Health:

```bash
curl http://localhost:8080/api/chat/health
```

Chat:

```bash
curl -X POST http://localhost:8080/api/chat \
  -H "Content-Type: application/json" \
  -d "{\"message\":\"build project\"}"
```

## 5) CI/CD Workflow

Workflow file:
- `.github/workflows/ci-cd.yml`

It supports:
- `push` and `pull_request`
- `workflow_dispatch` (required for chatbot-triggered real builds)

## 6) Notes

- If `GITHUB_TOKEN` is missing, project runs in safe demo mode.
- Frontend pipeline monitor reads latest GitHub Actions run and plays sounds.
- Sound toggle controls both sound effects and speech.
