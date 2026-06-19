param(
    [switch]$NoServe
)

$ErrorActionPreference = "Stop"
$projectRoot = Split-Path -Parent $PSScriptRoot

Push-Location $projectRoot
try {
    Write-Host "Running Maven test suite..." -ForegroundColor Cyan
    & mvn.cmd clean test
    if ($LASTEXITCODE -ne 0) {
        throw "Test suite failed. Skipping Allure serve step."
    }

    Write-Host "Generating Allure report..." -ForegroundColor Cyan
    & mvn.cmd -DskipTests=true allure:report
    if ($LASTEXITCODE -ne 0) {
        throw "Allure report generation failed."
    }

    if (-not $NoServe) {
        Write-Host "Starting Allure server in a new terminal..." -ForegroundColor Green
        Start-Process -FilePath "mvn.cmd" -ArgumentList "-DskipTests=true", "allure:serve" -WorkingDirectory $projectRoot | Out-Null
        Write-Host "Allure server command launched. Close that terminal window to stop the server." -ForegroundColor Green
    }

    Write-Host "Allure artifacts ready:" -ForegroundColor Yellow
    Write-Host " - Results: $projectRoot\allure\results"
    Write-Host " - Report:  $projectRoot\allure\report"
}
finally {
    Pop-Location
}

