param(
    [string]$BackendPath = "C:\Users\M597570\Documents\projetos\vacina-detalhes-api",
    [string]$FrontendPath = "C:\Users\M597570\Documents\projetos\patient-passport"
)

$ErrorActionPreference = "Stop"

function Test-RequiredCommand {
    param([string]$Name)

    if (-not (Get-Command $Name -ErrorAction SilentlyContinue)) {
        throw "Comando '$Name' nao encontrado no PATH."
    }
}

function Ensure-MavenAvailable {
    if (Get-Command "mvn" -ErrorAction SilentlyContinue) {
        return
    }

    $candidate = Get-ChildItem -Path (Join-Path $env:USERPROFILE ".maven") -Filter mvn.cmd -Recurse -ErrorAction SilentlyContinue |
        Sort-Object FullName -Descending |
        Select-Object -First 1

    if ($candidate) {
        $env:Path = "{0};{1}" -f $candidate.DirectoryName, $env:Path
    }

    if (-not (Get-Command "mvn" -ErrorAction SilentlyContinue)) {
        throw "Comando 'mvn' nao encontrado no PATH."
    }
}

Test-RequiredCommand -Name "java"
Test-RequiredCommand -Name "npm"
Test-RequiredCommand -Name "jpackage"
Ensure-MavenAvailable

if (-not (Test-Path (Join-Path $FrontendPath "package.json"))) {
    throw "Frontend nao encontrado em '$FrontendPath'."
}

if (-not (Test-Path (Join-Path $BackendPath "pom.xml"))) {
    throw "Backend nao encontrado em '$BackendPath'."
}

Write-Host "Preflight OK: java, npm, mvn e jpackage disponiveis."

