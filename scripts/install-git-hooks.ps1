$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$hooksPath = Join-Path $projectRoot ".githooks"

if (-not (Test-Path $hooksPath)) {
    throw "Hooks folder not found: $hooksPath"
}

Push-Location $projectRoot
try {
    git config core.hooksPath .githooks
    Write-Host "Configured git hooks path: .githooks" -ForegroundColor Green
}
finally {
    Pop-Location
}

