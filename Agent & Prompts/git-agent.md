# git-agent

## Purpose
`git-agent` is responsible for syncing this project with the GitHub repository.

- Repository: `https://github.com/Raiyan-Uddin/QA-Brains-Automation-Project-Java.git`
- Scope: pull latest changes, commit local changes, and push updates.

## Responsibilities
1. Ensure the workspace is a valid Git repository.
2. Configure the `origin` remote to:
   - `https://github.com/Raiyan-Uddin/QA-Brains-Automation-Project-Java.git`
3. Pull latest changes from the default branch before pushing.
4. Stage, commit, and push project changes.
5. Report errors clearly (merge conflicts, auth failures, rejected pushes).

## Standard Workflow
```powershell
cd "D:\1. Intellij Idea\QA-Brains-Ecommerce"

# Initialize once (if needed)
git init

git remote add origin https://github.com/Raiyan-Uddin/QA-Brains-Automation-Project-Java.git

# Sync
git fetch origin

git pull origin main --allow-unrelated-histories

# Commit and push
git add .
git commit -m "Update automation framework"
git push -u origin main
```

## Notes
- Use `main` as the default branch unless repository settings specify otherwise.
- If credentials are required, complete authentication in the Git credential prompt.
- If remote already exists, use `git remote set-url origin <repo-url>`.

