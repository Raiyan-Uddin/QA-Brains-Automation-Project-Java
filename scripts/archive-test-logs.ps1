param(
    [string]$LogsPath = "logs/test-runs",
    [string]$ArchiveRoot = "logs/test-runs/archive",
    [int]$OlderThanDays = 7,
    [switch]$WhatIfOnly
)

$ErrorActionPreference = "Stop"

function Write-Info($msg) { Write-Host "[INFO] $msg" -ForegroundColor Cyan }
function Write-Warn($msg) { Write-Host "[WARN] $msg" -ForegroundColor Yellow }
function Write-Ok($msg) { Write-Host "[OK]   $msg" -ForegroundColor Green }

if (-not (Test-Path $LogsPath)) {
    Write-Warn "Logs path not found: $LogsPath"
    exit 0
}

$cutoff = (Get-Date).AddDays(-$OlderThanDays)
$logs = Get-ChildItem -Path $LogsPath -File -Filter "*.log" | Where-Object { $_.LastWriteTime -lt $cutoff }

if ($logs.Count -eq 0) {
    Write-Info "No log files older than $OlderThanDays day(s) to archive."
    exit 0
}

if (-not (Test-Path $ArchiveRoot)) {
    New-Item -ItemType Directory -Path $ArchiveRoot -Force | Out-Null
}

$archived = 0
foreach ($log in $logs) {
    $dateFolder = Join-Path $ArchiveRoot $log.LastWriteTime.ToString("yyyy-MM-dd")
    if (-not (Test-Path $dateFolder)) {
        New-Item -ItemType Directory -Path $dateFolder -Force | Out-Null
    }

    $destination = Join-Path $dateFolder $log.Name

    if ($WhatIfOnly) {
        Write-Info "Would move: $($log.FullName) -> $destination"
        continue
    }

    Move-Item -Path $log.FullName -Destination $destination -Force
    $archived++
    Write-Ok "Archived: $($log.Name)"
}

if ($WhatIfOnly) {
    Write-Info "Dry run complete. Files were not moved."
} else {
    Write-Ok "Archive complete. Archived $archived file(s)."
}

