# Script: Open Failure Report and Diagnostics (PowerShell)
# Purpose: Quickly access failure diagnostics after test execution
# Usage: .\open-failure-report.ps1 or Run "PowerShell -ExecutionPolicy Bypass -File open-failure-report.ps1"

param(
    [ValidateSet('report', 'log', 'screenshots', 'source', 'folder', 'list', 'clean', 'summary')]
    [string]$Action = 'menu'
)

# Configuration
$FailuresDir = "docs/Test Reports/failures"
$HtmlReport = "$FailuresDir/failure-report.html"
$LogFile = "$FailuresDir/failure-diagnostics.log"
$ScreenshotsDir = "$FailuresDir/screenshots"
$PageSourceDir = "$FailuresDir/page-source"

# Colors
$Colors = @{
    'Success' = 'Green'
    'Error' = 'Red'
    'Info' = 'Cyan'
    'Warning' = 'Yellow'
}

function Write-ColorOutput {
    param(
        [string]$Message,
        [string]$Color = 'White'
    )
    Write-Host $Message -ForegroundColor $Color
}

function Show-Menu {
    Clear-Host
    Write-ColorOutput "======================================================================" $Colors.Info
    Write-ColorOutput "  QA Brains E-Commerce - Failure Report Viewer (PowerShell)" $Colors.Info
    Write-ColorOutput "======================================================================" $Colors.Info
    Write-Host ""

    # Check if failures directory exists
    if (-not (Test-Path $FailuresDir)) {
        Write-ColorOutput "[!] Failures directory not found: $FailuresDir" $Colors.Error
        Write-Host ""
        Write-ColorOutput "Please run tests first to generate failure reports." $Colors.Warning
        Write-Host ""
        Read-Host "Press Enter to exit"
        exit
    }

    if ($Action -eq 'menu') {
        Write-Host "Select an option:"
        Write-Host ""
        Write-Host "  1. Open Failure Report (HTML Dashboard)"
        Write-Host "  2. View Failure Log (Raw diagnostics)"
        Write-Host "  3. Open Screenshots Folder"
        Write-Host "  4. Open Page Source Folder"
        Write-Host "  5. Open Failures Directory"
        Write-Host "  6. List Recent Failed Tests"
        Write-Host "  7. Get Failure Summary"
        Write-Host "  8. Clean Old Failure Reports"
        Write-Host "  9. Exit"
        Write-Host ""
        $choice = Read-Host "Enter your choice (1-9)"
    } else {
        $choice = switch ($Action) {
            'report' { '1' }
            'log' { '2' }
            'screenshots' { '3' }
            'source' { '4' }
            'folder' { '5' }
            'list' { '6' }
            'summary' { '7' }
            'clean' { '8' }
            default { '9' }
        }
    }

    Handle-Choice $choice
}

function Handle-Choice {
    param([string]$Choice)

    switch ($Choice) {
        '1' { Open-FailureReport }
        '2' { View-FailureLog }
        '3' { Open-Screenshots }
        '4' { Open-PageSource }
        '5' { Open-FailuresFolder }
        '6' { List-RecentFailures }
        '7' { Show-FailureSummary }
        '8' { Clean-OldReports }
        '9' { Exit-Script }
        default {
            Clear-Host
            Write-ColorOutput "[!] Invalid option. Please enter 1-9." $Colors.Error
            Read-Host "Press Enter to continue"
            Show-Menu
        }
    }
}

function Open-FailureReport {
    Write-Host ""
    if (-not (Test-Path $HtmlReport)) {
        Write-ColorOutput "[!] Failure report not found: $HtmlReport" $Colors.Error
    } else {
        Write-ColorOutput "[*] Opening failure report in default browser..." $Colors.Info
        Start-Process $HtmlReport
        Write-ColorOutput "[OK] Report opened." $Colors.Success
    }
    Write-Host ""
    [void](Read-Host "Press Enter to continue")
}

function View-FailureLog {
    Write-Host ""
    if (-not (Test-Path $LogFile)) {
        Write-ColorOutput "[!] Failure log not found: $LogFile" $Colors.Error
    } else {
        Write-ColorOutput "[*] Opening failure log file..." $Colors.Info
        Invoke-Item $LogFile
        Write-ColorOutput "[OK] Log file opened." $Colors.Success
    }
    Write-Host ""
    [void](Read-Host "Press Enter to continue")
}

function Open-Screenshots {
    Write-Host ""
    if (-not (Test-Path $ScreenshotsDir)) {
        Write-ColorOutput "[!] Screenshots folder not found yet." $Colors.Warning
    } else {
        Write-ColorOutput "[*] Opening screenshots folder..." $Colors.Info
        Invoke-Item $ScreenshotsDir
        Write-ColorOutput "[OK] Folder opened." $Colors.Success
    }
    Write-Host ""
    [void](Read-Host "Press Enter to continue")
}

function Open-PageSource {
    Write-Host ""
    if (-not (Test-Path $PageSourceDir)) {
        Write-ColorOutput "[!] Page source folder not found yet." $Colors.Warning
    } else {
        Write-ColorOutput "[*] Opening page source folder..." $Colors.Info
        Invoke-Item $PageSourceDir
        Write-ColorOutput "[OK] Folder opened." $Colors.Success
    }
    Write-Host ""
    [void](Read-Host "Press Enter to continue")
}

function Open-FailuresFolder {
    Write-Host ""
    Write-ColorOutput "[*] Opening main failures directory..." $Colors.Info
    Invoke-Item $FailuresDir
    Write-ColorOutput "[OK] Folder opened." $Colors.Success
    Write-Host ""
    [void](Read-Host "Press Enter to continue")
}

function List-RecentFailures {
    Write-Host ""
    Write-ColorOutput "[*] Recent failed tests (last 20 screenshots):" $Colors.Info
    Write-Host ""

    if (Test-Path $ScreenshotsDir) {
        $files = Get-ChildItem $ScreenshotsDir -Filter "*.png" -File | Sort-Object LastWriteTime -Descending | Select-Object -First 20

        if ($files.Count -eq 0) {
            Write-ColorOutput "   No screenshots found." $Colors.Warning
        } else {
            foreach ($file in $files) {
                $size = [math]::Round($file.Length / 1KB, 2)
                Write-Host "   - $($file.Name) ($($size)KB) - Modified: $($file.LastWriteTime:yyyy-MM-dd HH:mm:ss)" -ForegroundColor Cyan
            }
        }
    } else {
        Write-ColorOutput "   Screenshots folder not found." $Colors.Warning
    }

    Write-Host ""
    [void](Read-Host "Press Enter to continue")
}

function Show-FailureSummary {
    Write-Host ""
    Write-ColorOutput "[*] Failure Summary:" $Colors.Info
    Write-Host ""

    # Screenshots count
    if (Test-Path $ScreenshotsDir) {
        $screenshotCount = (Get-ChildItem $ScreenshotsDir -Filter "*.png" -File | Measure-Object).Count
        Write-Host "   📸 Screenshots:     $screenshotCount"
    } else {
        Write-Host "   📸 Screenshots:     0"
    }

    # Page source count
    if (Test-Path $PageSourceDir) {
        $sourceCount = (Get-ChildItem $PageSourceDir -Filter "*.html" -File | Measure-Object).Count
        Write-Host "   📄 Page Source:     $sourceCount"
    } else {
        Write-Host "   📄 Page Source:     0"
    }

    # Log file size
    if (Test-Path $LogFile) {
        $logSize = [math]::Round((Get-Item $LogFile).Length / 1KB, 2)
        Write-Host "   📋 Log File Size:   $($logSize)KB"
    } else {
        Write-Host "   📋 Log File Size:   0KB"
    }

    # Total directory size
    $totalSize = (Get-ChildItem $FailuresDir -Recurse -File | Measure-Object -Property Length -Sum).Sum
    $totalMB = [math]::Round($totalSize / 1MB, 2)
    Write-ColorOutput "   💾 Total Size:      $($totalMB)MB" $Colors.Success

    Write-Host ""
    [void](Read-Host "Press Enter to continue")
}

function Clean-OldReports {
    Write-Host ""
    Write-ColorOutput "[*] Cleaning up old failure reports..." $Colors.Info
    Write-Host ""

    $daysOld = 30
    $cutoffDate = (Get-Date).AddDays(-$daysOld)

    Write-Host "   Files older than $daysOld days will be deleted."
    Write-Host "   Cutoff date: $($cutoffDate:yyyy-MM-dd)"
    Write-Host ""

    $response = Read-Host "Continue? (yes/no)"

    if ($response -eq "yes") {
        $deletedCount = 0

        # Clean screenshots
        if (Test-Path $ScreenshotsDir) {
            $oldScreenshots = Get-ChildItem $ScreenshotsDir -File | Where-Object { $_.LastWriteTime -lt $cutoffDate }
            foreach ($file in $oldScreenshots) {
                Remove-Item $file.FullName -Force
                $deletedCount++
            }
        }

        # Clean page source
        if (Test-Path $PageSourceDir) {
            $oldSource = Get-ChildItem $PageSourceDir -File | Where-Object { $_.LastWriteTime -lt $cutoffDate }
            foreach ($file in $oldSource) {
                Remove-Item $file.FullName -Force
                $deletedCount++
            }
        }

        Write-ColorOutput "[OK] Deleted $deletedCount old files." $Colors.Success
    } else {
        Write-ColorOutput "[*] Cleanup cancelled." $Colors.Info
    }

    Write-Host ""
    [void](Read-Host "Press Enter to continue")
}

function Exit-Script {
    Clear-Host
    Write-ColorOutput "Thank you for using the Failure Report Viewer!" $Colors.Success
    exit
}

# Run the menu
Show-Menu

