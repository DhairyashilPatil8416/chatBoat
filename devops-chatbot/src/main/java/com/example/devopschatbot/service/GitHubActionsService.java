package com.example.devopschatbot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GitHubActionsService {

    private final RestTemplate restTemplate;

    @Value("${github.token:}")
    private String token;

    @Value("${github.owner:DhairyashilPatil8416}")
    private String owner;

    @Value("${github.repo:chatBoat}")
    private String repo;

    @Value("${github.workflow:ci-cd.yml}")
    private String workflow;

    @Value("${github.branch:main}")
    private String branch;

    public GitHubActionsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    private HttpHeaders buildHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Accept", "application/vnd.github+json");
        headers.set("X-GitHub-Api-Version", "2022-11-28");
        if (token != null && !token.isBlank()) {
            headers.set("Authorization", "Bearer " + token);
        }
        return headers;
    }

    /** Triggers the CI/CD workflow via workflow_dispatch. */
    public String triggerBuild() {
        if (token == null || token.isBlank()) {
            return "Build pipeline started (demo mode – set GITHUB_TOKEN to trigger real builds)";
        }
        try {
            String url = "https://api.github.com/repos/" + owner + "/" + repo
                    + "/actions/workflows/" + workflow + "/dispatches";

            Map<String, Object> body = new HashMap<>();
            body.put("ref", branch);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, buildHeaders());
            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, request, Void.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return "Build pipeline started";
            }
            return "Could not trigger build. HTTP " + response.getStatusCode();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode().value() == 401 || e.getStatusCode().value() == 403) {
                return "GitHub token is invalid or missing 'actions:write' scope. Please check your GITHUB_TOKEN.";
            }
            return "GitHub API error: " + e.getStatusCode();
        } catch (Exception e) {
            return "Error triggering build: " + e.getMessage();
        }
    }

    /** Triggers the workflow with deploy target = AWS. */
    public String triggerDeploy() {
        if (token == null || token.isBlank()) {
            return "Deployment started on AWS (demo mode – set GITHUB_TOKEN to trigger real deployments)";
        }
        try {
            String url = "https://api.github.com/repos/" + owner + "/" + repo
                    + "/actions/workflows/" + workflow + "/dispatches";

            Map<String, Object> body = new HashMap<>();
            body.put("ref", branch);
            Map<String, String> inputs = new HashMap<>();
            inputs.put("deploy_env", "aws");
            body.put("inputs", inputs);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, buildHeaders());
            ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.POST, request, Void.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                return "Deployment started on AWS";
            }
            return "Could not trigger deployment. HTTP " + response.getStatusCode();
        } catch (HttpClientErrorException e) {
            return "GitHub API error: " + e.getStatusCode();
        } catch (Exception e) {
            return "Error triggering deployment: " + e.getMessage();
        }
    }

    /** Fetches the latest workflow run and returns a human-readable status. */
    @SuppressWarnings("unchecked")
    public String getLatestRunStatus() {
        try {
            String url = "https://api.github.com/repos/" + owner + "/" + repo
                    + "/actions/runs?per_page=1";

                HttpEntity<Void> request = new HttpEntity<>(buildHeaders());
                ResponseEntity<Map<String, Object>> response = restTemplate.exchange(url, HttpMethod.GET, request,
                    (Class<Map<String, Object>>) (Class<?>) Map.class);

                Map<String, Object> responseBody = response.getBody();
                if (responseBody == null) return "No pipeline runs found.";
                List<Map<String, Object>> runs = (List<Map<String, Object>>) responseBody.get("workflow_runs");
            if (runs == null || runs.isEmpty()) return "No pipeline runs found yet.";

            Map<String, Object> run = runs.get(0);
            String status     = (String) run.get("status");
            String conclusion = (String) run.get("conclusion");
            String runUrl     = (String) run.get("html_url");
            Object headCommit = run.get("head_commit");
            String commitMsg = "";
            if (headCommit instanceof Map<?, ?> headCommitMap) {
                Object message = headCommitMap.get("message");
                if (message instanceof String messageStr) {
                    commitMsg = messageStr.split("\\n")[0];
                }
            }

            if ("completed".equals(status) && "success".equals(conclusion)) {
                return "Build successful and deployed";
            } else if ("completed".equals(status) && "failure".equals(conclusion)) {
                return "Pipeline failed. View details: " + runUrl;
            } else if ("in_progress".equals(status)) {
                return "Pipeline is currently running: " + (commitMsg.isBlank() ? "" : "\"" + commitMsg + "\"");
            } else if ("queued".equals(status)) {
                return "Pipeline is queued and will start shortly.";
            }
            return "Pipeline status: " + status + (conclusion != null ? " – " + conclusion : "");
        } catch (Exception e) {
            return "Error fetching pipeline status: " + e.getMessage();
        }
    }
}
