# Git Workflow for CI/CD

## Branch model

- main: production-ready code
- develop: integration branch for upcoming release
- feature/<name>: new feature work
- hotfix/<name>: urgent production fix

## Day-to-day flow

1. Create feature branch from develop:

```bash
git checkout develop
git pull origin develop
git checkout -b feature/chat-response-improvements
```

2. Commit changes:

```bash
git add .
git commit -m "feat: improve chatbot response flow"
```

3. Push feature branch:

```bash
git push -u origin feature/chat-response-improvements
```

4. Open Pull Request:
- feature/* -> develop
- CI runs automatically from GitHub Actions.

5. Merge to develop after CI is green.

6. Release to production:
- Create PR: develop -> main
- Merge after approval
- CD job runs on push to main.

## Hotfix flow

1. Branch from main:

```bash
git checkout main
git pull origin main
git checkout -b hotfix/fix-deploy-command
```

2. Commit and push hotfix.
3. PR hotfix -> main (deploys after merge).
4. PR hotfix -> develop (keep branches synced).

## Commit message style

- feat: new feature
- fix: bug fix
- docs: documentation change
- chore: maintenance
- refactor: code restructure without behavior change

Examples:
- feat: add pipeline status command
- fix: handle empty chat message
