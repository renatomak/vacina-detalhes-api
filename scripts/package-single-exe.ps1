param(
    [string]$BackendPath = "C:\Users\M597570\Documents\projetos\vacina-detalhes-api",
    [string]$AppName = "VacinaDetalhesApi"
)

$ErrorActionPreference = "Stop"

if (-not (Get-Command "iexpress.exe" -ErrorAction SilentlyContinue)) {
    throw "iexpress.exe nao encontrado neste Windows."
}

$appImagePath = Join-Path $BackendPath "dist-exe\$AppName"
if (-not (Test-Path $appImagePath)) {
    throw "App-image nao encontrado em '$appImagePath'. Rode antes o package-fullstack-exe.ps1."
}

$portableRoot = Join-Path $BackendPath "dist-single"
$stagingPath = Join-Path $portableRoot "staging"
$targetExe = Join-Path $portableRoot "$AppName-Portable.exe"
$zipPath = Join-Path $stagingPath "$AppName.zip"
$launcherPath = Join-Path $stagingPath "launch-portable.cmd"
$sedPath = Join-Path $stagingPath "package.sed"

if (Test-Path $portableRoot) {
    Remove-Item -Path $portableRoot -Recurse -Force
}
New-Item -ItemType Directory -Path $stagingPath -Force | Out-Null

Compress-Archive -Path (Join-Path $appImagePath "*") -DestinationPath $zipPath -Force

$launcherContent = @'
@echo off
setlocal
set "APP_NAME=VacinaDetalhesApi"
set "TARGET=%LOCALAPPDATA%\%APP_NAME%Portable"
set "ARCHIVE=%~dp0%APP_NAME%.zip"

if not exist "%ARCHIVE%" (
  echo Arquivo de pacote nao encontrado: %ARCHIVE%
  pause
  exit /b 1
)

if exist "%TARGET%" rmdir /s /q "%TARGET%"
mkdir "%TARGET%"

powershell -NoProfile -ExecutionPolicy Bypass -Command "Expand-Archive -Path '%ARCHIVE%' -DestinationPath '%TARGET%' -Force"
if errorlevel 1 (
  echo Falha ao extrair arquivos para %TARGET%
  pause
  exit /b 1
)

for /f "tokens=5" %%p in ('netstat -ano ^| findstr /R /C:":8083 .*LISTENING"') do set "PORTPID=%%p"
if defined PORTPID (
  start "" http://localhost:8083
  exit /b 0
)

start "" "%TARGET%\%APP_NAME%.exe"

powershell -NoProfile -ExecutionPolicy Bypass -Command "$deadline=(Get-Date).AddSeconds(20); do { try { Invoke-WebRequest 'http://localhost:8083' -UseBasicParsing -TimeoutSec 2 | Out-Null; exit 0 } catch { Start-Sleep -Seconds 1 } } while((Get-Date) -lt $deadline); exit 0"
start "" http://localhost:8083
exit /b 0
'@
Set-Content -Path $launcherPath -Value $launcherContent -Encoding ASCII

$sedContent = @"
[Version]
Class=IEXPRESS
SEDVersion=3
[Options]
PackagePurpose=InstallApp
ShowInstallProgramWindow=0
HideExtractAnimation=1
UseLongFileName=1
InsideCompressed=0
CAB_FixedSize=0
CAB_ResvCodeSigning=0
RebootMode=N
InstallPrompt=
DisplayLicense=
FinishMessage=
TargetName=$targetExe
FriendlyName=$AppName Portable
AppLaunched=launch-portable.cmd
PostInstallCmd=<None>
AdminQuietInstCmd=
UserQuietInstCmd=
SourceFiles=SourceFiles
[Strings]
FILE0=launch-portable.cmd
FILE1=$AppName.zip
[SourceFiles]
SourceFiles0=$stagingPath
[SourceFiles0]
%FILE0%=
%FILE1%=
"@
Set-Content -Path $sedPath -Value $sedContent -Encoding ASCII

& iexpress.exe /N $sedPath | Out-Null
if ($LASTEXITCODE -ne 0 -or -not (Test-Path $targetExe)) {
    throw "Falha ao gerar executavel unico em '$targetExe'."
}

Write-Host "Executavel unico gerado em: $targetExe"

