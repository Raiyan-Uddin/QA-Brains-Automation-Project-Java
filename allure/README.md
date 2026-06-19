# Allure Reporting

This folder contains all Allure-related artifacts and helper scripts for this project.

## Structure
- `results/` - raw Allure result files (`*.json`, attachments) generated during test execution
- `report/` - generated static HTML report
- `.allure/` - local Allure CLI installation cache used by Maven plugin
- `run-suite-with-allure.ps1` - runs the suite, generates report, then starts Allure server

## Default behavior
- Running Maven tests (`mvn test` or `mvn clean test`) now automatically:
  - writes results to `allure/results`
  - generates report into `allure/report`

## Start local Allure server after a run
```powershell
.\allure\run-suite-with-allure.ps1
```

To run tests without opening the server:
```powershell
.\allure\run-suite-with-allure.ps1 -NoServe
```


