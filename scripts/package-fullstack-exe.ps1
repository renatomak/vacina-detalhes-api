param(
    [string]$BackendPath = "C:\Users\M597570\Documents\projetos\vacina-detalhes-api",
    [string]$FrontendPath = "C:\Users\M597570\Documents\projetos\patient-passport",
    [string]$AppName = "VacinaDetalhesApi",
    [switch]$SkipTests = $true
)

$ErrorActionPreference = "Stop"

$buildScript = Join-Path $PSScriptRoot "build-fullstack-jar.ps1"
& $buildScript -BackendPath $BackendPath -FrontendPath $FrontendPath -SkipTests:$SkipTests

$targetPath = Join-Path $BackendPath "target"
$jar = Get-ChildItem -Path (Join-Path $targetPath "*.jar") |
    Where-Object { $_.Name -notlike "*.original" } |
    Sort-Object LastWriteTime -Descending |
    Select-Object -First 1

if (-not $jar) {
    throw "Jar final nao encontrado para empacotamento."
}

$dest = Join-Path $BackendPath "dist-exe"
if (-not (Test-Path $dest)) {
    New-Item -ItemType Directory -Path $dest | Out-Null
}

$packageType = "exe"
if (-not (Get-Command "candle.exe" -ErrorAction SilentlyContinue) -or -not (Get-Command "light.exe" -ErrorAction SilentlyContinue)) {
    Write-Warning "WiX nao encontrado. Sera gerado app-image (com launcher .exe) em vez de instalador .exe."
    $packageType = "app-image"
}

$jpackageArgs = @(
    "--type", $packageType,
    "--name", $AppName,
    "--input", $targetPath,
    "--main-jar", $jar.Name,
    "--dest", $dest,
    "--win-console"
)

& jpackage @jpackageArgs

if ($LASTEXITCODE -ne 0) {
    throw "Falha ao gerar o executavel com jpackage."
}

Write-Host "Pacote gerado em: $dest (tipo: $packageType)"

if ($packageType -eq "app-image") {
    $singleExeScript = Join-Path $PSScriptRoot "package-single-exe.ps1"
    & $singleExeScript -BackendPath $BackendPath -AppName $AppName
}

