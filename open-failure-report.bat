@echo off
REM Script: Open Failure Report and Diagnostics
REM Purpose: Quickly access failure diagnostics after test execution
REM Usage: Double-click this file or run: open-failure-report.bat

setlocal enabledelayedexpansion

echo.
echo ======================================================================
echo   QA Brains E-Commerce - Failure Report Viewer
echo ======================================================================
echo.

set FAILURES_DIR=docs\Test Reports\failures
set HTML_REPORT=%FAILURES_DIR%\failure-report.html
set LOG_FILE=%FAILURES_DIR%\failure-diagnostics.log
set SCREENSHOTS_DIR=%FAILURES_DIR%\screenshots
set PAGE_SOURCE_DIR=%FAILURES_DIR%\page-source

REM Check if failures directory exists
if not exist "%FAILURES_DIR%" (
    echo [!] Failures directory not found: %FAILURES_DIR%
    echo.
    echo Please run tests first to generate failure reports.
    echo.
    pause
    exit /b 1
)

REM Check if HTML report exists
if not exist "%HTML_REPORT%" (
    echo [!] Failure report not generated yet.
    echo.
    echo The HTML report is only created when tests fail.
    echo Run your tests first, then try again.
    echo.
    pause
    exit /b 1
)

echo [OK] Found failure report at:
echo      %HTML_REPORT%
echo.

REM Display menu
echo Select an option:
echo.
echo  1. Open Failure Report (HTML Dashboard)
echo  2. View Failure Log (Raw diagnostics)
echo  3. Open Screenshots Folder
echo  4. Open Page Source Folder
echo  5. Open Failures Directory
echo  6. List Recent Failed Tests
echo  7. Clean Old Failure Reports (30+ days)
echo  8. Exit
echo.

set /p choice="Enter your choice (1-8): "

if "%choice%"=="1" (
    echo.
    echo [*] Opening failure report in default browser...
    start "" "%HTML_REPORT%"
    echo [OK] Report opened. Closing window...
    timeout /t 2 /nobreak
    goto end
)

if "%choice%"=="2" (
    echo.
    echo [*] Opening failure log...
    start "" "%LOG_FILE%"
    echo [OK] Log file opened.
    timeout /t 2 /nobreak
    goto end
)

if "%choice%"=="3" (
    echo.
    if not exist "%SCREENSHOTS_DIR%" (
        echo [!] No screenshots found yet.
    ) else (
        echo [*] Opening screenshots folder...
        start "" "%SCREENSHOTS_DIR%"
        echo [OK] Folder opened.
    )
    timeout /t 2 /nobreak
    goto end
)

if "%choice%"=="4" (
    echo.
    if not exist "%PAGE_SOURCE_DIR%" (
        echo [!] No page source files found yet.
    ) else (
        echo [*] Opening page source folder...
        start "" "%PAGE_SOURCE_DIR%"
        echo [OK] Folder opened.
    )
    timeout /t 2 /nobreak
    goto end
)

if "%choice%"=="5" (
    echo.
    echo [*] Opening main failures directory...
    start "" "%FAILURES_DIR%"
    echo [OK] Folder opened.
    timeout /t 2 /nobreak
    goto end
)

if "%choice%"=="6" (
    echo.
    echo [*] Recent failed tests (last 10 screenshots):
    echo.
    for /f %%F in ('dir "%SCREENSHOTS_DIR%" /b /o:-d /t:w ^| findstr /r "\.png$" 2^>nul') do (
        set "count=!count!1"
        if !count! leq 10 (
            echo   - %%F
        )
    )
    if not defined count (
        echo   No screenshots found.
    )
    echo.
    pause
    goto end
)

if "%choice%"=="7" (
    echo.
    echo [*] Finding failure reports older than 30 days...
    echo.

    REM Calculate date 30 days ago
    for /f %%A in ('powershell Get-Date -Format yyyyMMdd') do set today=%%A

    echo Warning: This will delete older failure reports.
    set /p confirm="Are you sure? (yes/no): "

    if /i "%confirm%"=="yes" (
        echo [*] Cleaning up old reports...
        REM Note: Batch file date comparison is complex, manual cleanup recommended
        echo.
        echo Recommended: Manually cleanup files older than 30 days from:
        echo    %FAILURES_DIR%\screenshots\
        echo    %FAILURES_DIR%\page-source\
        echo.
        echo Use Windows Explorer or PowerShell for easier date-based filtering.
    )

    echo.
    pause
    goto end
)

if "%choice%"=="8" (
    goto end
)

echo.
echo [!] Invalid option. Please enter 1-8.
echo.
pause
goto menu

:end
cls
echo.
echo Thank you for using the Failure Report Viewer!
echo.

