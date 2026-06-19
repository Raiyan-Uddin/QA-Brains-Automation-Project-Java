# git-agent

## Purpose
`git-agent` is responsible for syncing this project with **both** GitHub repositories.

| Remote    | Repository URL |
|-----------|----------------|
| `origin`  | `https://github.com/Raiyan-Uddin/QA-Brains-Automation-Project-Java.git` |
| `origin2` | `https://github.com/raiyan437/QA-Brains-Automation-Project-Java-Playwright.git` |

- Scope: pull latest changes, commit local changes, and push updates to **both** remotes every time.

## Responsibilities
1. Ensure the workspace is a valid Git repository.
2. Ensure both remotes are configured correctly (see table above).
3. Pull latest changes from `origin/main` before pushing.
4. Stage all changes, commit with a meaningful message, and push to **both** `origin` and `origin2`.
5. Report errors clearly (merge conflicts, auth failures, rejected pushes).

## Standard Workflow
```powershell
cd "D:\1. Intellij Idea\QA-Brains-Ecommerce"

# --- One-time setup (run once; skip if already configured) ---
git init

git remote add origin  https://github.com/Raiyan-Uddin/QA-Brains-Automation-Project-Java.git
git remote add origin2 https://github.com/raiyan437/QA-Brains-Automation-Project-Java-Playwright.git

# If remotes already exist but URL is wrong:
git remote set-url origin  https://github.com/Raiyan-Uddin/QA-Brains-Automation-Project-Java.git
git remote set-url origin2 https://github.com/raiyan437/QA-Brains-Automation-Project-Java-Playwright.git

# --- Sync + Push (run every time) ---
git fetch origin
git pull origin main --allow-unrelated-histories

git add .
git commit -m "Update automation framework"

# Push to BOTH repos
git push -u origin  main
git push -u origin2 main
```

## Notes
- **Always push to both remotes** — `origin` (Raiyan-Uddin account) and `origin2` (raiyan437 account).
- Use `main` as the default branch unless repository settings specify otherwise.
- If credentials are required, complete authentication in the Git credential prompt for each remote.
- Do NOT skip `origin2` — both repos must stay in sync after every push.
