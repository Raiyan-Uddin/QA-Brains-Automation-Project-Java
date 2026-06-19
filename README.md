# QA Brains E-Commerce — Playwright Automation Project

## Tech Stack
| Technology   | Version   | Purpose                    |
|-------------|-----------|----------------------------|
| Java         | 17+       | Programming Language       |
| Playwright   | 1.48.0    | Browser Automation         |
| TestNG       | 7.10.2    | Test Framework             |
| Maven        | 3.9+      | Build & Dependency Mgmt    |
| POM Pattern  | -         | Page Object Model          |

## Project Layout
- `src/` - test framework source code
- `docs/` - reports, test cases, and implementation notes
- `docs/project-notes/` - moved implementation/rating summary docs
- `logs/test-runs/` - moved run logs (`*.log`)
- `scripts/` - maintenance utilities (for example, log archiving)
- `open-failure-report.bat` / `open-failure-report.ps1` - quick report launcher scripts

## Quick Navigation
- Main reports folder: `docs/Test Reports/`
- Latest full-run report (auto-updated): `docs/Test Reports/Test-Report-Latest.html`
- Failure report artifacts: `docs/Test Reports/failures/`
- Allure results: `allure/results/`
- Allure static report: `allure/report/`
- Git auth workflow: `docs/GIT_AUTH.md`
- Project notes index: `docs/project-notes/INDEX.md`
- Implementation notes map: `docs/project-notes/IMPLEMENTATION_INDEX.md`
- Ratings index: `docs/project-notes/RATING_INDEX.md`
- Root quick start: `QUICK_START.md`

## Full Suite Report Behavior
When you run tests, the framework now refreshes this shared report file automatically:

- `docs/Test Reports/Test-Report-Latest.html`

This file is overwritten with the latest execution summary.

## Log Maintenance
Archive old run logs into date-based folders:

```powershell
.\scripts\archive-test-logs.ps1 -OlderThanDays 7
```

Dry run preview (no file moves):

```powershell
.\scripts\archive-test-logs.ps1 -OlderThanDays 7 -WhatIfOnly
```

## Build Check
Use Maven as usual from project root:

```bash
mvn clean test
```

## Run Suite + Auto Serve Allure
```powershell
.\allure\run-suite-with-allure.ps1
```

## Install Git Hooks
```powershell
.\scripts\install-git-hooks.ps1
```

