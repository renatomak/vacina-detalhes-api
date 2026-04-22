# Backend - Consulta Paciente + Vacinas

API REST em Java 17 + Spring Boot para consulta de pacientes e vacinas aplicadas.

## Arquitetura

Estrutura em estilo hexagonal:

- `controller/`: adaptadores de entrada HTTP
- `service/`: casos de uso (portas de entrada)
- `repository/`: portas de saida
- `repository/jdbc/`: adaptadores de saida com `JdbcTemplate`
- `mapper/`: `RowMapper` para DTOs
- `dto/`: contratos JSON de resposta
- `exception/`: tratamento global de erro padronizado

## Endpoints

- `GET /api/pacientes/search?query=...`
  - Se query tiver 11 digitos: busca por CPF e retorna `PacienteDTO` (404 se nao encontrado)
  - Caso contrario: busca por nome e retorna `List<PacienteResumoDTO>`
- `GET /api/pacientes/{id}`
- `GET /api/pacientes/{id}/vacinas`
- `GET /api/vacinas/aplicacoes/{idAplicacao}`

## Configuracao (env vars)

A aplicacao le configuracao de `src/main/resources/application.yml` e permite override por variaveis de ambiente:

- `DB_URL`
- `DB_USERNAME`
- `DB_PASSWORD`
- `DB_POOL_MAX_SIZE` (padrao `5`)
- `DB_POOL_MIN_IDLE` (padrao `1`)
- `DB_POOL_CONNECTION_TIMEOUT_MS` (padrao `30000`)
- `PORT` (padrao `8083`)
- `SEARCH_NOME_LIMIT`
- `CORS_ALLOWED_ORIGINS`

## Swagger

- UI: `/swagger-ui.html`
- OpenAPI JSON: `/v3/api-docs`

## Gerar JAR

```bash
mvn clean package
```

## Rodar local (sem Docker)

```bash
mvn spring-boot:run
```

## Rodar local com profile dedicado

Foi adicionado o arquivo `src/main/resources/application-local.yml` para facilitar testes locais com PostgreSQL no `localhost`.

Valores configurados no profile `local`:

- banco: `jdbc:postgresql://localhost:5432/goiania_saude`
- usuario: `postgres`
- senha: `postgres`
- porta da API: `8083`

Se sua senha do Postgres for diferente, ajuste o valor em `application-local.yml`.

Para subir usando esse profile:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

Ou, se preferir rodar o jar:

```bash
java -jar target/*.jar --spring.profiles.active=local
```

## Deploy em VM com Docker

### 1) Build da imagem

```bash
docker build -t vacinas-api:latest .
```

### 2) Run do container

```bash
docker run -d --name vacinas-api \
  -p 8083:8083 \
  -e PORT=8083 \
  -e DB_URL="jdbc:postgresql://SEU_HOST:5432/SEU_BANCO" \
  -e DB_USERNAME="SEU_USUARIO" \
  -e DB_PASSWORD="SUA_SENHA" \
  -e DB_POOL_MAX_SIZE=5 \
  vacinas-api:latest
```

### 3) Validacao

```bash
curl http://SEU_HOST_PUBLICO:8083/v3/api-docs
curl http://SEU_HOST_PUBLICO:8083/swagger-ui.html
```

## Deploy no Render

O projeto possui `Dockerfile` e `render.yaml` para facilitar o deploy.

### 1) Publicar codigo no GitHub

Crie um repositorio e envie o conteudo do projeto (incluindo `render.yaml`).

### 2) Criar o Web Service

- No Render, clique em **New +** -> **Blueprint** (ou **Web Service**).
- Conecte o repositorio.
- Se usar Blueprint, o Render lera automaticamente o `render.yaml`.

### 3) Configurar variaveis de ambiente

No servico, configure:

- `DB_URL` (formato JDBC, ex.: `jdbc:postgresql://HOST:5432/goiania_saude?sslmode=require`)
- `DB_USERNAME`
- `DB_PASSWORD`
- `CORS_ALLOWED_ORIGINS` (dominio do frontend)

Ja existe valor padrao para:

- `PORT=8083`
- `SEARCH_NOME_LIMIT=50`

### 4) Validacao

Com o servico em status **Live**, valide:

```bash
curl https://SEU-SERVICO.onrender.com/v3/api-docs
curl https://SEU-SERVICO.onrender.com/swagger-ui.html
```

