param(
    [string]$BackendPath = "C:\Users\M597570\Documents\projetos\vacina-detalhes-api",
    [string]$FrontendPath = "C:\Users\M597570\Documents\projetos\patient-passport",
    [switch]$SkipTests = $true
)

$ErrorActionPreference = "Stop"

$preflightScript = Join-Path $PSScriptRoot "preflight-exe.ps1"
& $preflightScript -BackendPath $BackendPath -FrontendPath $FrontendPath

$frontendTempPath = Join-Path $env:TEMP "patient-passport-build"
if (Test-Path $frontendTempPath) {
    Remove-Item -Path $frontendTempPath -Recurse -Force
}
New-Item -ItemType Directory -Path $frontendTempPath | Out-Null

robocopy $FrontendPath $frontendTempPath /MIR /XD node_modules dist .git | Out-Null
if ($LASTEXITCODE -gt 7) {
    throw "Falha ao preparar copia temporaria do frontend. Codigo robocopy: $LASTEXITCODE"
}

Push-Location $frontendTempPath
try {
    if (Test-Path "package-lock.json") {
        npm ci
    } else {
        npm install
    }

    if ($LASTEXITCODE -ne 0) {
        Write-Warning "Falha ao instalar dependencias do frontend. Tentando novamente com limpeza de node_modules."
        if (Test-Path "node_modules") {
            Remove-Item -Path "node_modules" -Recurse -Force
        }
        npm install
        if ($LASTEXITCODE -ne 0) {
            throw "Falha ao instalar dependencias do frontend."
        }
    }

    npm run build
    if ($LASTEXITCODE -ne 0) {
        throw "Falha no build do frontend."
    }
} finally {
    Pop-Location
}

$distPath = Join-Path $frontendTempPath "dist"
if (-not (Test-Path $distPath)) {
    throw "Pasta 'dist' do frontend nao foi gerada."
}

$staticPath = Join-Path $BackendPath "src\main\resources\static"
if (-not (Test-Path $staticPath)) {
    New-Item -ItemType Directory -Path $staticPath | Out-Null
} else {
    Get-ChildItem -Path $staticPath -Force | Remove-Item -Recurse -Force
}

Copy-Item -Path (Join-Path $distPath "*") -Destination $staticPath -Recurse -Force
Write-Host "Frontend copiado para '$staticPath'."

Push-Location $BackendPath
try {
    $mvnArgs = @("clean", "package")
    if ($SkipTests) {
        $mvnArgs += "-DskipTests"
    }

    & mvn @mvnArgs
    if ($LASTEXITCODE -ne 0) {
        throw "Falha no build Maven."
    }
} finally {
    Pop-Location
}

$jar = Get-ChildItem -Path (Join-Path $BackendPath "target\*.jar") |
    Where-Object { $_.Name -notlike "*.original" } |
    Sort-Object LastWriteTime -Descending |
    Select-Object -First 1

if (-not $jar) {
    throw "Jar final nao encontrado em '$BackendPath\target'."
}

Write-Host "Build OK. Jar gerado: $($jar.FullName)"

