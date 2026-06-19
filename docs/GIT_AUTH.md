# Git Authentication Workflow

This project uses a single-account Git workflow by default.

## Default account and remote
- Remote: `origin`
- URL: `https://github.com/Raiyan-Uddin/QA-Brains-Automation-Project-Java.git`
- Preferred auth strategy: **Git Credential Manager (GCM) browser login**

## One-time setup (recommended)
```powershell
git remote set-url origin https://github.com/Raiyan-Uddin/QA-Brains-Automation-Project-Java.git
git config --global credential.helper manager
git config --global --unset credential.https://github.com.useHttpPath
```

## Daily sync commands
```powershell
git fetch origin
git pull origin main --allow-unrelated-histories
git add .
git commit -m "Update automation framework"
git push origin main
```

## Recovery steps (if push hangs or fails with 403)
1. Confirm remote:
```powershell
git remote -v
```
2. Remove stale GitHub credentials from Windows Credential Manager:
```powershell
cmdkey /list | Select-String "github"
cmdkey /delete:"git:https://github.com"
```
3. Re-authenticate with GCM by running a push:
```powershell
git push origin main
```
4. Complete the browser login for the `Raiyan-Uddin` account when prompted.

## Security note
- Do not embed PATs in remote URLs.
- Keep credentials in GCM only.

