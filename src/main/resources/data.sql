-- ==========================================================================
-- DATA.SQL — Ambiente local/test (PostgreSQL embutido)
-- Recria o schema simplificado (sem FK, sem triggers) e insere dados de teste
-- ==========================================================================

-- 1. DROP em ordem inversa ------------------------------------------------
DROP TABLE IF EXISTS atendimento_prontuario;
DROP TABLE IF EXISTS aih;
DROP TABLE IF EXISTS atendimento;
DROP TABLE IF EXISTS tabela_cbo;
DROP TABLE IF EXISTS classificacao_risco;
DROP TABLE IF EXISTS natureza_procura_tp_atendimento;
DROP TABLE IF EXISTS tipo_atendimento;
DROP TABLE IF EXISTS vac_aplicacao;
DROP TABLE IF EXISTS rnds_integracao_vacina;
DROP TABLE IF EXISTS produto_vacina;
DROP TABLE IF EXISTS produtos;
DROP TABLE IF EXISTS fabricante_medicamento;
DROP TABLE IF EXISTS via_administracao;
DROP TABLE IF EXISTS local_aplicacao;
DROP TABLE IF EXISTS grupo_atendimento_vacinacao_esus;
DROP TABLE IF EXISTS profissional;
DROP TABLE IF EXISTS orgao_emissor;
DROP TABLE IF EXISTS empresa;
DROP TABLE IF EXISTS tipo_vacina;
DROP TABLE IF EXISTS calendario;
DROP TABLE IF EXISTS usuario_cadsus;
DROP TABLE IF EXISTS endereco_usuario_cadsus;
DROP TABLE IF EXISTS tipo_logradouro_cadsus;
DROP TABLE IF EXISTS etnia_indigena;
DROP TABLE IF EXISTS raca;
DROP TABLE IF EXISTS nacionalidade;
DROP TABLE IF EXISTS cidade;
DROP TABLE IF EXISTS estado;

-- 2. CRIAÇÃO DAS TABELAS --------------------------------------------------

CREATE TABLE estado (
    cod_est   INTEGER PRIMARY KEY,
    sigla     VARCHAR(2) NOT NULL
);

CREATE TABLE cidade (
    cod_cid   INTEGER PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL,
    cod_est   INTEGER NOT NULL
);

CREATE TABLE tipo_logradouro_cadsus (
    cd_tipo_logradouro INTEGER PRIMARY KEY,
    ds_tipo_logradouro VARCHAR(50) NOT NULL
);

CREATE TABLE endereco_usuario_cadsus (
    cd_endereco        BIGINT PRIMARY KEY,
    keyword            VARCHAR(10),
    nm_logradouro      VARCHAR(100),
    nm_comp_logradouro VARCHAR(50),
    nr_logradouro      VARCHAR(10),
    cep                VARCHAR(10),
    nm_bairro          VARCHAR(50),
    cod_cid            INTEGER NOT NULL,
    cd_tipo_logradouro INTEGER NOT NULL
);

CREATE TABLE raca (
    cd_raca     SMALLINT PRIMARY KEY,
    ds_raca     VARCHAR(30) NOT NULL,
    "version"   BIGINT DEFAULT 0 NOT NULL,
    version_all BIGINT NOT NULL
);

CREATE TABLE etnia_indigena (
    cd_etnia    BIGINT PRIMARY KEY,
    ds_etnia    VARCHAR(100) NOT NULL,
    "version"   BIGINT NOT NULL,
    version_all BIGINT NOT NULL
);

CREATE TABLE nacionalidade (
    cd_pais     INTEGER PRIMARY KEY,
    ds_pais     VARCHAR(50) NOT NULL,
    "version"   BIGINT DEFAULT 0 NOT NULL,
    version_all BIGINT NOT NULL
);

CREATE TABLE usuario_cadsus (
    cd_usu_cadsus            NUMERIC(8)  PRIMARY KEY,
    nm_usuario               VARCHAR(70) NOT NULL,
    sg_sexo                  CHAR(1)     NOT NULL,
    nm_mae                   VARCHAR(70),
    nm_pai                   VARCHAR(70),
    email                    VARCHAR(100),
    cpf                      VARCHAR(14),
    dt_nascimento            DATE        NOT NULL,
    cd_pais_nascimento       INTEGER,
    cd_raca                  SMALLINT,
    cd_etnia                 BIGINT,
    nr_telefone              VARCHAR(15),
    nr_telefone_2            VARCHAR(15),
    apelido                  VARCHAR(50),
    cod_cid_nascimento       BIGINT,
    cd_endereco              BIGINT,
    dt_inclusao              DATE        NOT NULL DEFAULT CURRENT_DATE,
    dt_preenchimento_form    DATE        NOT NULL DEFAULT CURRENT_DATE,
    st_excluido              SMALLINT    NOT NULL DEFAULT 0,
    "version"                BIGINT      DEFAULT 0 NOT NULL,
    dt_usuario               TIMESTAMP   NOT NULL DEFAULT NOW(),
    cd_usuario               NUMERIC(6)  NOT NULL DEFAULT 1,
    cd_usuario_cad           NUMERIC(6)  NOT NULL DEFAULT 1,
    flag_utiliza_nome_social SMALLINT    NOT NULL DEFAULT 0,
    flag_unificado           SMALLINT    NOT NULL DEFAULT 0,
    flag_outras_pop_nomades  SMALLINT    NOT NULL DEFAULT 0,
    version_all              BIGINT      NOT NULL DEFAULT 1
);

CREATE TABLE tipo_vacina (
    cd_vacina   INTEGER PRIMARY KEY,
    ds_vacina   VARCHAR(200) NOT NULL,
    cod_gru     INTEGER      NOT NULL,
    cod_sub     INTEGER      NOT NULL,
    "version"   BIGINT       DEFAULT 0 NOT NULL,
    version_all BIGINT       DEFAULT 1 NOT NULL
);

CREATE TABLE calendario (
    cd_calendario      BIGINT PRIMARY KEY,
    ds_calendario      VARCHAR(50) NOT NULL,
    "version"          BIGINT,
    padrao             CHAR(1)  NOT NULL DEFAULT 'S',
    flag_atualizacao   SMALLINT NOT NULL DEFAULT 0,
    flag_aprazamento   SMALLINT NOT NULL DEFAULT 0,
    cod_estrategia_pni SMALLINT NOT NULL DEFAULT 0
);

CREATE TABLE fabricante_medicamento (
    cd_fabricante INTEGER PRIMARY KEY,
    ds_fabricante VARCHAR(50) NOT NULL,
    "version"     BIGINT DEFAULT 0 NOT NULL,
    cnpj          VARCHAR(15)
);

CREATE TABLE produtos (
    cod_pro                                VARCHAR(13) PRIMARY KEY,
    cod_uni                                NUMERIC(12) NOT NULL DEFAULT 1,
    cod_gru                                INTEGER     NOT NULL DEFAULT 1,
    cod_sub                                INTEGER     NOT NULL DEFAULT 1,
    descricao                              VARCHAR(200) NOT NULL,
    cont_min                               CHAR(1)     NOT NULL DEFAULT 'N',
    usuario                                INTEGER     NOT NULL DEFAULT 1,
    dt_usuario                             DATE        NOT NULL DEFAULT CURRENT_DATE,
    dt_cadastro                            DATE        NOT NULL DEFAULT CURRENT_DATE,
    "version"                              BIGINT      DEFAULT 0 NOT NULL,
    flag_permite_disp_mais                 VARCHAR(1)  NOT NULL DEFAULT 'N',
    flag_emprestimo                        SMALLINT    NOT NULL DEFAULT 0,
    flag_tratamento_prolongado_antibiotico SMALLINT    NOT NULL DEFAULT 0,
    flag_ativo                             SMALLINT    NOT NULL DEFAULT 1,
    flag_disp_ped_lic                      SMALLINT    NOT NULL DEFAULT 0,
    cd_fabricante                          INTEGER,
    fabricante_esus                        VARCHAR(150)
);

CREATE TABLE produto_vacina (
    cd_produto_vacina BIGINT PRIMARY KEY,
    cod_pro           VARCHAR(13) NOT NULL,
    cd_vacina         INTEGER     NOT NULL,
    qt_dose           SMALLINT    NOT NULL DEFAULT 1,
    "version"         BIGINT      DEFAULT 0 NOT NULL
);

CREATE TABLE empresa (
    empresa           INTEGER PRIMARY KEY,
    descricao         VARCHAR(60) NOT NULL,
    fantasia          VARCHAR(60),
    cod_cid           BIGINT      NOT NULL DEFAULT 1,
    "version"         BIGINT      DEFAULT 0 NOT NULL,
    cnes              VARCHAR(7),
    acesso_restrito   SMALLINT    NOT NULL DEFAULT 0,
    situacao_bloqueio SMALLINT    NOT NULL DEFAULT 0,
    telefone          VARCHAR(20)
);

CREATE TABLE orgao_emissor (
    cd_orgao_emissor SMALLINT PRIMARY KEY,
    ds_orgao_emissor VARCHAR(60) NOT NULL,
    sg_orgao_emissor VARCHAR(10),
    "version"        BIGINT      DEFAULT 0 NOT NULL,
    version_all      BIGINT      NOT NULL DEFAULT 1
);

CREATE TABLE profissional (
    cd_profissional INTEGER PRIMARY KEY,
    nm_profissional VARCHAR(60) NOT NULL,
    nr_registro     VARCHAR(50),
    cd_cns          VARCHAR(60),
    cd_con_classe   SMALLINT,
    "version"       BIGINT NOT NULL DEFAULT 0,
    version_all     BIGINT NOT NULL DEFAULT 1
);

CREATE TABLE via_administracao (
    cd_via_administracao BIGINT PRIMARY KEY,
    descricao            VARCHAR NOT NULL,
    "version"            BIGINT  NOT NULL DEFAULT 1
);

CREATE TABLE local_aplicacao (
    cd_local_aplicacao BIGINT PRIMARY KEY,
    descricao          VARCHAR NOT NULL,
    "version"          BIGINT  NOT NULL DEFAULT 1
);

CREATE TABLE grupo_atendimento_vacinacao_esus (
    cd_grupo_atendimento_vac_esus BIGINT PRIMARY KEY,
    descricao                     VARCHAR NOT NULL,
    "version"                     BIGINT  NOT NULL DEFAULT 1
);

CREATE TABLE rnds_integracao_vacina (
    cd_rnds_integracao_vacina BIGINT     PRIMARY KEY,
    cd_vac_aplicacao          BIGINT     NOT NULL,
    uuid_rnds                 VARCHAR,
    uuid_origem               VARCHAR    NOT NULL DEFAULT '',
    situacao                  SMALLINT   NOT NULL DEFAULT 0,
    cd_usuario                NUMERIC(6) NOT NULL DEFAULT 1,
    dt_usuario                TIMESTAMP  NOT NULL DEFAULT NOW(),
    "version"                 BIGINT     NOT NULL DEFAULT 1
);

CREATE TABLE tabela_cbo (
    cd_cbo  VARCHAR(10) PRIMARY KEY,
    ds_cbo  VARCHAR(200) NOT NULL
);

CREATE TABLE classificacao_risco (
    cd_classificacao_risco INTEGER PRIMARY KEY,
    descricao              VARCHAR(100) NOT NULL
);

CREATE TABLE tipo_atendimento (
    cd_tp_atendimento   INTEGER PRIMARY KEY,
    ds_tipo_atendimento VARCHAR(200) NOT NULL
);

CREATE TABLE natureza_procura_tp_atendimento (
    cd_nat_proc_tp_atendimento INTEGER PRIMARY KEY,
    cd_tp_atendimento          INTEGER NOT NULL
);

CREATE TABLE atendimento (
    nr_atendimento             BIGINT     PRIMARY KEY,
    cd_usu_cadsus              NUMERIC(8) NOT NULL,
    empresa                    INTEGER    NOT NULL,
    cd_profissional            INTEGER,
    cd_cbo                     VARCHAR(10),
    classificacao_risco        INTEGER,
    cd_nat_proc_tp_atendimento INTEGER,
    dt_chegada                 TIMESTAMP,
    dt_atendimento             TIMESTAMP,
    status                     SMALLINT   NOT NULL DEFAULT 5
);

CREATE TABLE atendimento_prontuario (
    id             BIGINT    PRIMARY KEY,
    nr_atendimento BIGINT    NOT NULL,
    data           TIMESTAMP NOT NULL,
    tipo_registro  SMALLINT  NOT NULL,
    descricao      TEXT
);

CREATE TABLE aih (
    nr_atendimento        BIGINT    PRIMARY KEY,
    cd_profissional       INTEGER,
    dt_cadastro           TIMESTAMP,
    principais_sinais     TEXT,
    condicoes_just_intern TEXT,
    principais_resultados TEXT,
    diagnostico_inicial   TEXT
);

CREATE TABLE vac_aplicacao (
    cd_vac_aplicacao          BIGINT      PRIMARY KEY,
    cd_usu_cadsus             NUMERIC(8)  NOT NULL,
    cd_vacina                 INTEGER,
    ds_vacina                 VARCHAR(200) NOT NULL,
    cd_usuario                NUMERIC(6)  NOT NULL DEFAULT 1,
    dt_aplicacao              TIMESTAMP,
    dt_cadastro               TIMESTAMP   NOT NULL DEFAULT NOW(),
    status                    SMALLINT    NOT NULL DEFAULT 0,
    observacao                VARCHAR(1024),
    cd_calendario             BIGINT,
    cd_estrategia             BIGINT,
    lote                      VARCHAR(20),
    cd_produto_vacina         BIGINT,
    empresa                   INTEGER     NOT NULL,
    dt_validade               TIMESTAMP,
    nr_atendimento            BIGINT,
    cd_profissional_aplicacao INTEGER,
    grupo_atendimento         SMALLINT    NOT NULL DEFAULT 0,
    flag_gestante             SMALLINT    NOT NULL DEFAULT 0,
    novo_frasco               SMALLINT    NOT NULL DEFAULT 0,
    cd_doses                  SMALLINT,
    flag_historico            SMALLINT    NOT NULL DEFAULT 0,
    flag_puerpera             SMALLINT,
    flag_fora_esquema_vacinal SMALLINT    NOT NULL DEFAULT 0,
    turno                     SMALLINT,
    local_atendimento         SMALLINT,
    viajante                  SMALLINT,
    cd_via_administracao      BIGINT,
    cd_local_aplicacao        BIGINT,
    "version"                 BIGINT      NOT NULL DEFAULT 1,
    version_all               BIGINT,
    comunicante_hanseniase    SMALLINT    NOT NULL DEFAULT 0,
    status_baixa              SMALLINT    NOT NULL DEFAULT 0
);

-- 3. DADOS BASE -----------------------------------------------------------

INSERT INTO estado (cod_est, sigla) VALUES
    (1, 'GO'),
    (2, 'SP'),
    (3, 'RJ');

INSERT INTO cidade (cod_cid, descricao, cod_est) VALUES
    (151, 'GOIANIA',              1),
    (152, 'APARECIDA DE GOIANIA', 1),
    (200, 'SAO PAULO',            2);

INSERT INTO tipo_logradouro_cadsus (cd_tipo_logradouro, ds_tipo_logradouro) VALUES
    (81, 'RUA'),
    (82, 'AVENIDA'),
    (83, 'TRAVESSA');

INSERT INTO endereco_usuario_cadsus
    (cd_endereco, keyword, nm_logradouro, nm_comp_logradouro, nr_logradouro, cep, nm_bairro, cod_cid, cd_tipo_logradouro)
VALUES
    (7230412, 'K1', 'C 149',    'QD 325 LT 11', 'SN',   '74230050', 'JARDIM AMERICA',              151, 81),
    (7230413, 'K2', 'AVENIDA GOIAS', NULL,       '1500', '74000100', 'CENTRO',                      151, 82),
    (7230415, 'K4', 'RB 51 A',  'QD 51 LT 72',  'SN',   '74474396', 'Residencial Recanto do Bosque', 520870, 81);

INSERT INTO raca (cd_raca, ds_raca, "version", version_all) VALUES
    (1, 'BRANCA',   1, 1),
    (2, 'PRETA',    1, 2),
    (3, 'AMARELA',  1, 3),
    (4, 'PARDA',    1, 4),
    (5, 'INDIGENA', 1, 5);

INSERT INTO etnia_indigena (cd_etnia, ds_etnia, "version", version_all) VALUES
    (1, 'Nao Informada', 1, 1),
    (2, 'Guarani',       1, 2),
    (3, 'Tupi',          1, 3);

INSERT INTO nacionalidade (cd_pais, ds_pais, "version", version_all) VALUES
    (1, 'BRASIL',    1, 1),
    (2, 'ARGENTINA', 1, 2),
    (3, 'PORTUGAL',  1, 3);

INSERT INTO usuario_cadsus (
    cd_usu_cadsus, nm_usuario, sg_sexo, nm_mae, nm_pai, email, cpf, dt_nascimento,
    cd_pais_nascimento, cd_raca, cd_etnia, nr_telefone, nr_telefone_2, apelido,
    cod_cid_nascimento, cd_endereco
) VALUES
    (3033700, 'MARIA EDUARDA HUMMEL OLIVEIRA', 'F',
     'ANA PAULA HUMMEL OLIVEIRA', 'ADRIANO DE OLIVEIRA',
     'mariaeduarda@email.com', '05342621180', '2006-05-18',
     1, 1, 1, '62991632742', '62991632743', 'MARIA EDUARDA', 151, 7230412),
    (9282479, 'THALLYA VALENTINA GOMES RAMOS MOCK', 'F',
     'NAYANNE GOMES SODRE RAMOS', 'MACKSON DE SOUSA RAMOS',
     NULL, '12169441131', '2024-06-30',
     1, 4, NULL, '62998702201', NULL, NULL, 520870, 7230415);

INSERT INTO tipo_vacina (cd_vacina, ds_vacina, cod_gru, cod_sub, "version", version_all) VALUES
    (1, 'COVID-19',       1, 1, 1, 1),
    (2, 'INFLUENZA',      1, 2, 1, 2),
    (3, 'FEBRE AMARELA',  1, 3, 1, 3),
    (4, 'HEPATITE B',     2, 1, 1, 4),
    (5, 'TETANO',         2, 2, 1, 5);

INSERT INTO calendario (cd_calendario, ds_calendario, "version", padrao, flag_atualizacao, flag_aprazamento, cod_estrategia_pni) VALUES
    (1, 'Rotina',   1, 'S', 0, 0, 1),
    (2, 'Campanha', 1, 'N', 0, 0, 2),
    (3, 'Especial', 1, 'N', 0, 0, 3);

INSERT INTO fabricante_medicamento (cd_fabricante, ds_fabricante, "version", cnpj) VALUES
    (1, 'PFIZER',         1, '58006628000120'),
    (2, 'ASTRAZENECA',    1, '60318797000117'),
    (3, 'BUTANTAN',       1, '53373484000113'),
    (4, 'BIO-MANGUINHOS', 1, '33781055000135');

INSERT INTO produtos (
    cod_pro, cod_uni, cod_gru, cod_sub, descricao, cont_min,
    usuario, dt_usuario, dt_cadastro, "version",
    flag_permite_disp_mais, flag_emprestimo, flag_tratamento_prolongado_antibiotico,
    flag_ativo, flag_disp_ped_lic, cd_fabricante, fabricante_esus
) VALUES
    ('COMIRNATY01', 1, 1, 1, 'COMIRNATY - COVID-19 BNT162b2',       'N', 1, CURRENT_DATE, CURRENT_DATE, 1, 'N', 0, 0, 1, 0, 1, 'Pfizer-BioNTech'),
    ('VACFLU01',    1, 1, 2, 'VACINA INFLUENZA TRIVALENTE',          'N', 1, CURRENT_DATE, CURRENT_DATE, 1, 'N', 0, 0, 1, 0, 3, 'Butantan'),
    ('FAMAMAR01',   1, 1, 3, 'VACINA FEBRE AMARELA 17DD ATENUADA',   'N', 1, CURRENT_DATE, CURRENT_DATE, 1, 'N', 0, 0, 1, 0, 4, 'Bio-Manguinhos');

INSERT INTO produto_vacina (cd_produto_vacina, cod_pro, cd_vacina, qt_dose, "version") VALUES
    (1, 'COMIRNATY01', 1, 2, 1),
    (2, 'VACFLU01',    2, 1, 1),
    (3, 'FAMAMAR01',   3, 1, 1);

INSERT INTO empresa (empresa, descricao, fantasia, cod_cid, "version", cnes, acesso_restrito, situacao_bloqueio) VALUES
    (1, 'UBS JARDIM AMERICA', 'UBS Jardim America', 151, 1, '1234567', 0, 0),
    (2, 'UBS CENTRO',         'UBS Centro',         151, 1, '7654321', 0, 0);

INSERT INTO orgao_emissor (cd_orgao_emissor, ds_orgao_emissor, sg_orgao_emissor, "version", version_all) VALUES
    (1, 'CONSELHO FEDERAL DE MEDICINA',   'CRM',   1, 1),
    (2, 'CONSELHO FEDERAL DE ENFERMAGEM', 'COREN', 1, 2);

INSERT INTO profissional (cd_profissional, nm_profissional, nr_registro, cd_cns, cd_con_classe, "version", version_all) VALUES
    (1, 'DR. JOAO DA SILVA', 'CRM-GO 12345',   '123456789012345', 1, 1, 1),
    (2, 'ENF. MARIA SOUZA',  'COREN-GO 98765', '987654321098765', 2, 1, 2);

INSERT INTO via_administracao (cd_via_administracao, descricao, "version") VALUES
    (1, 'INTRAMUSCULAR', 1),
    (2, 'SUBCUTANEA',    1),
    (3, 'ORAL',          1),
    (4, 'INTRADERMICA',  1);

INSERT INTO local_aplicacao (cd_local_aplicacao, descricao, "version") VALUES
    (1, 'BRACO DIREITO',  1),
    (2, 'BRACO ESQUERDO', 1),
    (3, 'COXA DIREITA',   1),
    (4, 'COXA ESQUERDA',  1);

INSERT INTO grupo_atendimento_vacinacao_esus (cd_grupo_atendimento_vac_esus, descricao, "version") VALUES
    (1, 'POPULACAO GERAL',        1),
    (2, 'IDOSOS',                 1),
    (3, 'GESTANTES',              1),
    (4, 'PROFISSIONAIS DE SAUDE', 1);

-- 4. APLICAÇÕES DE VACINA (paciente 9282479 — Thallya Valentina Gomes Ramos) ----------
-- (lookup data: fabricantes 6-9, tipo_vacina 6-13, produtos, profissionais 4-7 inseridos na seção 9)

INSERT INTO vac_aplicacao (
    cd_vac_aplicacao, cd_usu_cadsus, cd_vacina, ds_vacina, cd_usuario,
    dt_aplicacao, dt_cadastro, status, observacao,
    cd_calendario, cd_estrategia, lote, cd_produto_vacina,
    empresa, dt_validade, cd_profissional_aplicacao,
    grupo_atendimento, flag_gestante, novo_frasco, cd_doses,
    flag_historico, flag_puerpera, flag_fora_esquema_vacinal,
    turno, local_atendimento, viajante,
    cd_via_administracao, cd_local_aplicacao,
    "version", comunicante_hanseniase, status_baixa
) VALUES
    -- VACINA POLIO INJETÁVEL 3ª Dose (2025-02-10)
    (712879059, 9282479, 6, 'VACINA POLIO INJETÁVEL', 1,
     '2025-02-10 09:00:00', NOW(), 0, NULL,
     NULL, NULL, 'POLIO25C', 5, 3, '2026-12-31 00:00:00', 4,
     1, 0, 0, 3, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 0),
    -- VACINA PENTA (DTP/HEPB/HIB) 3ª Dose — PANACEA (2025-02-10)
    (712878831, 9282479, 7, 'VACINA PENTA (DTP/HEPB/HIB)', 1,
     '2025-02-10 09:00:00', NOW(), 0, NULL,
     NULL, NULL, 'PENTA25A', 6, 3, '2026-12-31 00:00:00', 4,
     1, 0, 0, 3, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 0),
    -- VACINA MENINGO C 2ª Dose — GSK (2025-01-06)
    (696637875, 9282479, 8, 'VACINA MENINGO C', 1,
     '2025-01-06 10:00:00', NOW(), 0, NULL,
     NULL, NULL, 'MENC25A', 8, 3, '2026-12-31 00:00:00', 5,
     1, 0, 0, 2, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 0),
    -- VACINA ROTAVIRUS 2ª Dose — FIOCRUZ (2024-11-25)
    (680761498, 9282479, 9, 'VACINA ROTAVIRUS', 1,
     '2024-11-25 08:00:00', NOW(), 0, NULL,
     NULL, NULL, 'ROTA24B', 9, 3, '2025-12-31 00:00:00', 4,
     1, 0, 0, 2, 0, 0, 0, 1, 1, 0, 3, 1, 1, 0, 0),
    -- VACINA POLIO INJETÁVEL 2ª Dose — FIOCRUZ (2024-11-25)
    (680760572, 9282479, 6, 'VACINA POLIO INJETÁVEL', 1,
     '2024-11-25 08:00:00', NOW(), 0, NULL,
     NULL, NULL, 'POLIO24B', 5, 3, '2025-12-31 00:00:00', 4,
     1, 0, 0, 2, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 0),
    -- VACINA PNEUMO 10 2ª Dose — FIOCRUZ (2024-11-25)
    (680750914, 9282479, 10, 'VACINA PNEUMO 10', 1,
     '2024-11-25 08:00:00', NOW(), 0, NULL,
     NULL, NULL, 'PNEUMO24B', 10, 3, '2025-12-31 00:00:00', 4,
     1, 0, 0, 2, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 0),
    -- VACINA PENTA (DTP/HEPB/HIB) 2ª Dose — SERUM INSTITUTE (2024-11-25)
    (680749995, 9282479, 7, 'VACINA PENTA (DTP/HEPB/HIB)', 1,
     '2024-11-25 08:00:00', NOW(), 0, NULL,
     NULL, NULL, 'PENTA24B', 7, 3, '2025-12-31 00:00:00', 4,
     1, 0, 0, 2, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 0),
    -- Vacina meningo ACWY 1ª Dose — FIOCRUZ (2024-10-14)
    (663571473, 9282479, 11, 'Vacina meningo ACWY', 1,
     '2024-10-14 08:00:00', NOW(), 0, NULL,
     NULL, NULL, 'MENGOACWY24A', 11, 3, '2025-12-31 00:00:00', 6,
     1, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 0),
    -- VACINA ROTAVIRUS 1ª Dose — FIOCRUZ (2024-09-24)
    (655145207, 9282479, 9, 'VACINA ROTAVIRUS', 1,
     '2024-09-24 09:00:00', NOW(), 0, NULL,
     NULL, NULL, 'ROTA24A', 9, 3, '2025-12-31 00:00:00', 6,
     1, 0, 0, 1, 0, 0, 0, 1, 1, 0, 3, 1, 1, 0, 0),
    -- VACINA PNEUMO 10 1ª Dose — FIOCRUZ (2024-09-24)
    (655144905, 9282479, 10, 'VACINA PNEUMO 10', 1,
     '2024-09-24 09:00:00', NOW(), 0, NULL,
     NULL, NULL, 'PNEUMO24A', 10, 3, '2025-12-31 00:00:00', 6,
     1, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 0),
    -- VACINA PENTA (DTP/HEPB/HIB) 1ª Dose — PANACEA (2024-09-24)
    (655144042, 9282479, 7, 'VACINA PENTA (DTP/HEPB/HIB)', 1,
     '2024-09-24 09:00:00', NOW(), 0, NULL,
     NULL, NULL, 'PENTA24A', 6, 3, '2025-12-31 00:00:00', 6,
     1, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 0),
    -- VACINA POLIO INJETÁVEL 1ª Dose — FIOCRUZ (2024-09-24)
    (655144614, 9282479, 6, 'VACINA POLIO INJETÁVEL', 1,
     '2024-09-24 09:00:00', NOW(), 0, NULL,
     NULL, NULL, 'POLIO24A', 5, 3, '2025-12-31 00:00:00', 6,
     1, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 0),
    -- VACINA BCG Dose Única — SERUM INSTITUTE (2024-07-05)
    (621443710, 9282479, 12, 'VACINA BCG', 1,
     '2024-07-05 08:00:00', NOW(), 0, NULL,
     NULL, NULL, 'BCG24A', 12, 3, '2026-12-31 00:00:00', 7,
     1, 0, 0, 5, 0, 0, 0, 1, 1, 0, 4, 1, 1, 0, 0),
    -- VACINA HEPATITE B 2º Reforço — FUNDACAO BUTANTAN (2024-07-05)
    (621443669, 9282479, 13, 'VACINA HEPATITE B', 1,
     '2024-07-05 08:00:00', NOW(), 0, NULL,
     NULL, NULL, 'HEPB24A', 13, 3, '2026-12-31 00:00:00', 7,
     1, 0, 0, 8, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 0);

-- 5. APLICAÇÕES (paciente 3033700 — Maria Eduarda Hummel Oliveira) --------

INSERT INTO vac_aplicacao (
    cd_vac_aplicacao, cd_usu_cadsus, cd_vacina, ds_vacina, cd_usuario,
    dt_aplicacao, dt_cadastro, status, observacao,
    cd_calendario, cd_estrategia, lote, cd_produto_vacina,
    empresa, dt_validade, cd_profissional_aplicacao,
    grupo_atendimento, flag_gestante, novo_frasco, cd_doses,
    flag_historico, flag_puerpera, flag_fora_esquema_vacinal,
    turno, local_atendimento, viajante,
    cd_via_administracao, cd_local_aplicacao,
    "version", comunicante_hanseniase, status_baixa
) VALUES
    (2001, 3033700, 4, 'HEPATITE B', 1,
     '2020-03-01 08:00:00', NOW(), 0, NULL,
     1, 1, 'HB001', NULL, 1, '2023-06-30 00:00:00', 1,
     1, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 1, 1, 0, 0),
    (2002, 3033700, 5, 'TETANO', 1,
     '2021-07-15 09:00:00', NOW(), 0, NULL,
     1, 1, 'TET2021', NULL, 2, '2026-12-31 00:00:00', 2,
     1, 0, 0, 6, 0, 0, 0, 1, 2, 0, 1, 2, 1, 0, 0);

-- 6. RNDS INTEGRAÇÃO ------------------------------------------------------

INSERT INTO rnds_integracao_vacina
    (cd_rnds_integracao_vacina, cd_vac_aplicacao, uuid_rnds, uuid_origem, situacao, cd_usuario, dt_usuario, "version")
VALUES
    (1, 712879059, 'uuid-polio-3dose',    'origem-712879059', 1, 1, NOW(), 1),
    (2, 712878831, 'uuid-penta-3dose',    'origem-712878831', 1, 1, NOW(), 1),
    (3, 2001,      'uuid-hepb-maria',     'origem-2001',      1, 1, NOW(), 1);

-- 7. DADOS ADICIONAIS para paciente 5128061 (DORVALINA FERREIRA DA MOTA MOCK) -----

-- Cidade de Goiânia com código IBGE
INSERT INTO cidade (cod_cid, descricao, cod_est) VALUES
    (520870, 'GOIANIA', 1);

-- Endereço da paciente
INSERT INTO endereco_usuario_cadsus
    (cd_endereco, keyword, nm_logradouro, nm_comp_logradouro, nr_logradouro, cep, nm_bairro, cod_cid, cd_tipo_logradouro)
VALUES
    (7230414, 'K3', 'CV18', 'RESIDENCIAL CENTER VILLE', 'SN', '74905450', 'RESIDENCIAL CENTER VILLE', 520870, 81);

-- Paciente 5128061
INSERT INTO usuario_cadsus (
    cd_usu_cadsus, nm_usuario, sg_sexo, nm_mae, nm_pai, email, cpf, dt_nascimento,
    cd_pais_nascimento, cd_raca, cd_etnia, nr_telefone, nr_telefone_2, apelido,
    cod_cid_nascimento, cd_endereco
) VALUES
    (5128061, 'DORVALINA FERREIRA DA MOTA MOCK', 'F',
     'FRANCISCA FERREIRA DA MOTA', NULL,
     NULL, '96748257115', '1952-11-11',
     1, 4, NULL, '62984061234', NULL, NULL, 520870, 7230414);

-- Empresa: Secretaria Municipal de Saúde
INSERT INTO empresa (empresa, descricao, fantasia, cod_cid, "version", cnes, acesso_restrito, situacao_bloqueio) VALUES
    (3, 'SECRETARIA MUNICIPAL DE SAUDE DE GOIANIA', 'SECRETARIA MUNICIPAL DE SAUDE', 520870, 1, '2337545', 0, 0);

-- Fabricante: Fundação Butantan
INSERT INTO fabricante_medicamento (cd_fabricante, ds_fabricante, "version", cnpj) VALUES
    (5, 'FUNDACAO BUTANTAN', 1, '60501293000176');

-- Produto de vacina Influenza (Fundação Butantan)
INSERT INTO produtos (
    cod_pro, cod_uni, cod_gru, cod_sub, descricao, cont_min,
    usuario, dt_usuario, dt_cadastro, "version",
    flag_permite_disp_mais, flag_emprestimo, flag_tratamento_prolongado_antibiotico,
    flag_ativo, flag_disp_ped_lic, cd_fabricante, fabricante_esus
) VALUES
    ('VACFLU02', 1, 1, 2, 'VACINA INFLUENZA TRIVALENTE INJETAVEL', 'N', 1, CURRENT_DATE, CURRENT_DATE, 1, 'N', 0, 0, 1, 0, 5, 'Fundacao Butantan');

INSERT INTO produto_vacina (cd_produto_vacina, cod_pro, cd_vacina, qt_dose, "version") VALUES
    (4, 'VACFLU02', 2, 1, 1);

-- Profissional aplicador
INSERT INTO profissional (cd_profissional, nm_profissional, nr_registro, cd_cns, cd_con_classe, "version", version_all) VALUES
    (3, 'ROSANGELA MENESES DE BARROS', 'COREN-GO 108291', '898001627080007', 2, 1, 3);

-- 8. APLICAÇÕES DE VACINA (paciente 5128061 — Dorvalina Ferreira da Mota MOCK) -----

INSERT INTO vac_aplicacao (
    cd_vac_aplicacao, cd_usu_cadsus, cd_vacina, ds_vacina, cd_usuario,
    dt_aplicacao, dt_cadastro, status, observacao,
    cd_calendario, cd_estrategia, lote, cd_produto_vacina,
    empresa, dt_validade, cd_profissional_aplicacao,
    grupo_atendimento, flag_gestante, novo_frasco, cd_doses,
    flag_historico, flag_puerpera, flag_fora_esquema_vacinal,
    turno, local_atendimento, viajante,
    cd_via_administracao, cd_local_aplicacao,
    "version", comunicante_hanseniase, status_baixa
) VALUES
    -- Influenza Dose Única (2025)
    (747298245, 5128061, 2, 'VACINA INFLUENZA TRIVALENTE', 1,
     '2025-04-24 09:00:00', NOW(), 0, NULL,
     2, 2, 'FLU2025B', 4, 3, '2025-12-31 00:00:00', 3,
     2, 0, 0, 5, 0, 0, 0, 1, 1, 0, 2, 1, 1, 0, 0),
    -- Febre Amarela Dose Única (2007 — histórico)
    (6948661, 5128061, 3, 'VACINA FEBRE AMARELA (ATENUADA)', 1,
     '2007-05-28 10:00:00', NOW(), 0, NULL,
     1, 1, 'FA2007X', 3, 3, '2012-05-28 00:00:00', 3,
     1, 0, 0, 5, 1, 0, 0, 1, 1, 0, 2, 1, 1, 0, 0);

-- 9. LOOKUP DATA para vacinas de 9282479 (Thallya) -------------------------

INSERT INTO fabricante_medicamento (cd_fabricante, ds_fabricante, "version", cnpj) VALUES
    (6, 'FUNDACAO OSWALDO CRUZ - FIOCRUZ',       1, '33781055000135'),
    (7, 'PANACEA BIOTEC LTDA',                    1, NULL),
    (8, 'GLAXOSMITHKLINE VACCINES S.R.L',         1, NULL),
    (9, 'SERUM INSTITUTE OF INDIA LTD. - S.INDIA',1, NULL);

INSERT INTO tipo_vacina (cd_vacina, ds_vacina, cod_gru, cod_sub, "version", version_all) VALUES
    ( 6, 'VACINA POLIO INJETÁVEL',       3, 1, 1,  6),
    ( 7, 'VACINA PENTA (DTP/HEPB/HIB)', 3, 2, 1,  7),
    ( 8, 'VACINA MENINGO C',             3, 3, 1,  8),
    ( 9, 'VACINA ROTAVIRUS',             3, 4, 1,  9),
    (10, 'VACINA PNEUMO 10',             3, 5, 1, 10),
    (11, 'VACINA MENINGO ACWY',          3, 6, 1, 11),
    (12, 'VACINA BCG',                   3, 7, 1, 12),
    (13, 'VACINA HEPATITE B',            2, 3, 1, 13);

INSERT INTO produtos (
    cod_pro, cod_uni, cod_gru, cod_sub, descricao, cont_min,
    usuario, dt_usuario, dt_cadastro, "version",
    flag_permite_disp_mais, flag_emprestimo, flag_tratamento_prolongado_antibiotico,
    flag_ativo, flag_disp_ped_lic, cd_fabricante, fabricante_esus
) VALUES
    ('VPOLIO_FIO',     1, 3, 1, 'VACINA POLIO INJETÁVEL (FIOCRUZ)',          'N', 1, CURRENT_DATE, CURRENT_DATE, 1, 'N', 0, 0, 1, 0, 6, 'FIOCRUZ'),
    ('VPENTA_PAN',     1, 3, 2, 'VACINA PENTA (PANACEA BIOTEC)',              'N', 1, CURRENT_DATE, CURRENT_DATE, 1, 'N', 0, 0, 1, 0, 7, 'Panacea Biotec'),
    ('VPENTA_SER',     1, 3, 2, 'VACINA PENTA (SERUM INSTITUTE)',             'N', 1, CURRENT_DATE, CURRENT_DATE, 1, 'N', 0, 0, 1, 0, 9, 'Serum Institute'),
    ('VMENGOC_GSK',    1, 3, 3, 'VACINA MENINGO C (GLAXOSMITHKLINE)',         'N', 1, CURRENT_DATE, CURRENT_DATE, 1, 'N', 0, 0, 1, 0, 8, 'GlaxoSmithKline'),
    ('VROTAV_FIO',     1, 3, 4, 'VACINA ROTAVIRUS (FIOCRUZ)',                 'N', 1, CURRENT_DATE, CURRENT_DATE, 1, 'N', 0, 0, 1, 0, 6, 'FIOCRUZ'),
    ('VPNEUMO_FIO',    1, 3, 5, 'VACINA PNEUMO 10 (FIOCRUZ)',                 'N', 1, CURRENT_DATE, CURRENT_DATE, 1, 'N', 0, 0, 1, 0, 6, 'FIOCRUZ'),
    ('VMENACWY_FIO', 1, 3, 6, 'VACINA MENINGO ACWY (FIOCRUZ)',              'N', 1, CURRENT_DATE, CURRENT_DATE, 1, 'N', 0, 0, 1, 0, 6, 'FIOCRUZ'),
    ('VBCG_SER',       1, 3, 7, 'VACINA BCG (SERUM INSTITUTE)',               'N', 1, CURRENT_DATE, CURRENT_DATE, 1, 'N', 0, 0, 1, 0, 9, 'Serum Institute'),
    ('VHEPB_BUT',      1, 2, 3, 'VACINA HEPATITE B (FUNDACAO BUTANTAN)',      'N', 1, CURRENT_DATE, CURRENT_DATE, 1, 'N', 0, 0, 1, 0, 5, 'Fundacao Butantan');

INSERT INTO produto_vacina (cd_produto_vacina, cod_pro, cd_vacina, qt_dose, "version") VALUES
    ( 5, 'VPOLIO_FIO',      6, 1, 1),
    ( 6, 'VPENTA_PAN',      7, 1, 1),
    ( 7, 'VPENTA_SER',      7, 1, 1),
    ( 8, 'VMENGOC_GSK',     8, 1, 1),
    ( 9, 'VROTAV_FIO',      9, 1, 1),
    (10, 'VPNEUMO_FIO',    10, 1, 1),
    (11, 'VMENACWY_FIO', 11, 1, 1),
    (12, 'VBCG_SER',       12, 1, 1),
    (13, 'VHEPB_BUT',      13, 1, 1);

INSERT INTO profissional (cd_profissional, nm_profissional, nr_registro, cd_cns, cd_con_classe, "version", version_all) VALUES
    (4, 'ADRIANA BORGES RIBEIRO CARRIJO',  'COREN-GO 7742',  NULL, 2, 1, 4),
    (5, 'MARIA GEOVANIA DA SILVA CONCEICAO','COREN-GO 841432', NULL, 2, 1, 5),
    (6, 'MARIA ANITA RODRIGUES DOS SANTOS', 'COREN-GO 551858', NULL, 2, 1, 6),
    (7, 'KELLY MARIA DE PAULA SOBRINHO',    'COREN-GO 099551', NULL, 2, 1, 7);

-- 10. PRONTUÁRIO — tabelas de referência e atendimentos (paciente 9282479) ---

-- Unidades de saúde (empresas 4-9)
INSERT INTO empresa (empresa, descricao, fantasia, cod_cid, "version", cnes, acesso_restrito, situacao_bloqueio, telefone) VALUES
    (4, 'USF BRISAS DA MATA',                                     'USF BRISAS DA MATA',         520870, 1, '7018977', 0, 0, '06235243006'),
    (5, 'UPA MARIA PIRES PERILLO - UPA NOROESTE',                 'UPA NOROESTE',               520870, 1, '3624969', 0, 0, '6235243461'),
    (6, 'UPA - FINSOCIAL',                                        'UPA FINSOCIAL',              520870, 1, '5604591', 0, 0, '6235243533'),
    (7, 'CENTRO DE SAUDE NORTE FERROVIARIO',                      'CS NORTE FERROVIARIO',       520870, 1, '2337731', 0, 0, '6235241921'),
    (8, 'CENTRO DE SAUDE SETOR PERIM BENEDITO DOS SANTOS VIEIRA', 'CS SETOR PERIM',             520870, 1, '2337820', 0, 0, NULL),
    (9, 'USF ALTO DO VALE',                                       'USF ALTO DO VALE',           520870, 1, '5539490', 0, 0, '6235243503');

-- CBO (Classificação Brasileira de Ocupações)
INSERT INTO tabela_cbo (cd_cbo, ds_cbo) VALUES
    ('322250', 'AUXILIAR DE ENFERMAGEM DA ESTRATEGIA DE SAUDE DA FAMILIA'),
    ('223505', 'Enfermeiro'),
    ('322245', 'TECNICO DE ENFERMAGEM DA ESTRATEGIA DE SAUDE DA FAMILIA'),
    ('223565', 'ENFERMEIRO DA ESTRATEGIA DE SAUDE DA FAMILIA'),
    ('225124', 'MEDICO PEDIATRA'),
    ('322205', 'Técnico de enfermagem'),
    ('225142', 'MEDICO DA ESTRATEGIA DE SAUDE DA FAMILIA'),
    ('225125', 'MEDICO CLINICO');

-- Classificação de risco
INSERT INTO classificacao_risco (cd_classificacao_risco, descricao) VALUES
    (1, 'Não Urgente'),
    (2, 'Pouco Urgente'),
    (3, 'Urgente'),
    (4, 'Muito Urgente'),
    (5, 'Emergência');

-- Tipos de atendimento
INSERT INTO tipo_atendimento (cd_tp_atendimento, ds_tipo_atendimento) VALUES
    ( 1, 'APS - CONSULTA DE ENFERMAGEM'),
    ( 2, 'APS - CONSULTA MÉDICA'),
    ( 3, 'USF - ACOLHIMENTO À DEMANDA ESPONTÂNEA'),
    ( 4, 'UPA - CLASSIFICAÇÃO DE RISCO'),
    ( 5, 'UPA - ATENDIMENTO INFANTIL'),
    ( 6, 'UPA - OBSERVAÇÃO - INFANTIL'),
    ( 7, 'APS - ACOMPANHAMENTO BENEFICIÁRIO BOLSA FAMILIA'),
    ( 8, 'APS - VACINA'),
    ( 9, 'APS - ACOLHIMENTO'),
    (10, 'APS - CONSULTA PEDIATRIA'),
    (11, 'APS - SALA DE PROCEDIMENTOS'),
    (12, 'APS - ACOLHIMENTO À DEMANDA ESPONTÂNEA');

-- Natureza/Tipo de atendimento (1:1 mapeamento)
INSERT INTO natureza_procura_tp_atendimento (cd_nat_proc_tp_atendimento, cd_tp_atendimento) VALUES
    ( 1,  1),( 2,  2),( 3,  3),( 4,  4),( 5,  5),( 6,  6),
    ( 7,  7),( 8,  8),( 9,  9),(10, 10),(11, 11),(12, 12);

-- Profissionais dos atendimentos (8-19)
INSERT INTO profissional (cd_profissional, nm_profissional, nr_registro, cd_cns, cd_con_classe, "version", version_all) VALUES
    ( 8, 'DIANA FERREIRA DE SOUZA',              'COREN-GO 622698',  NULL, 2, 1,  8),
    ( 9, 'LUIS FELIPE PIRES FONTANA',             'CRM-GO 28160',     NULL, 1, 1,  9),
    (10, 'SELOMITE BERNARDES DE MORAES',          'COREN-GO 178992',  NULL, 2, 1, 10),
    (11, 'MARLON HORA MARTINS',                   'CRM-GO 35135',     NULL, 1, 1, 11),
    (12, 'ERIKA KAREM GOMES DA SILVA ARAUJO',     'COREN-GO 214785',  NULL, 2, 1, 12),
    (13, 'MARIA APARECIDA DE LIMA',               'COREN-GO 165805',  NULL, 2, 1, 13),
    (14, 'EDNA PEREIRA DOS SANTOS',               'COREN-GO 758895',  NULL, 2, 1, 14),
    (15, 'JAMES NOGUEIRA PIMENTA',                'CRM-GO 10062',     NULL, 1, 1, 15),
    (16, 'ANA CAROLINA MATIAS FERREIRA',          'CRM-GO 27907',     NULL, 1, 1, 16),
    (17, 'ISABEL MADALENA DE SOUZA TRINDADE',     'COREN-GO 115701',  NULL, 2, 1, 17),
    (18, 'LUCIA GONCALVES DA SILVA',              'COREN-GO 267171',  NULL, 2, 1, 18),
    (19, 'RODRIGO CAETANO DE ALMEIDA',            'CRM-GO 6445',      NULL, 1, 1, 19);

-- Atendimentos do paciente 9282479 (34 registros)
INSERT INTO atendimento
    (nr_atendimento, cd_usu_cadsus, empresa, cd_profissional, cd_cbo,
     classificacao_risco, cd_nat_proc_tp_atendimento,
     dt_chegada, dt_atendimento, status)
VALUES
    (102064248, 9282479,  4, NULL,  NULL, 1,  1, '2025-04-24 10:37:53', '2025-04-24 11:30:00', 5),
    (101951432, 9282479,  4, NULL,  NULL, 1,  2, '2025-04-23 11:44:33', '2025-04-23 12:30:00', 5),
    (101949229, 9282479,  4,    5, '322250', 1, 3, '2025-04-23 11:44:33', '2025-04-23 12:00:00', 5),
    (101677656, 9282479,  5,    8, '223505', 2, 4, '2025-04-20 07:12:54', '2025-04-20 08:00:00', 5),
    (101678211, 9282479,  5,    9, '225124', 2, 5, '2025-04-20 07:12:54', '2025-04-20 08:30:00', 5),
    (101678612, 9282479,  5,    9, '225124', 2, 6, '2025-04-20 07:12:54', '2025-04-20 09:00:00', 5),
    (101679191, 9282479,  5, NULL,  NULL,    2, 6, '2025-04-20 07:12:54', '2025-04-20 09:30:00', 5),
    (101620758, 9282479,  6,   10, '223505', 2, 4, '2025-04-18 18:17:29', '2025-04-18 18:30:00', 5),
    (101620969, 9282479,  6,   11, '225125', 2, 5, '2025-04-18 18:17:29', '2025-04-18 19:00:00', 5),
    (101621788, 9282479,  6, NULL,  NULL,    2, 6, '2025-04-18 18:17:29', '2025-04-18 19:30:00', 5),
    ( 99418682, 9282479,  6,   12, '223505', 2, 4, '2025-03-24 19:05:40', '2025-03-24 19:45:00', 5),
    ( 99420002, 9282479,  6,    9, '225125', 2, 5, '2025-03-24 19:05:40', '2025-03-24 20:10:00', 5),
    ( 99422704, 9282479,  6, NULL,  NULL,    2, 6, '2025-03-24 19:05:40', '2025-03-24 20:30:00', 5),
    ( 97062779, 9282479,  4,   13, '223565', 1, 1, '2025-02-24 11:38:22', '2025-02-24 12:00:00', 5),
    ( 96196958, 9282479,  4,    5, '322250', 1, 3, '2025-02-14 08:46:39', '2025-02-14 09:00:00', 5),
    ( 96196827, 9282479,  4,   13, '223565', 1, 7, '2025-02-14 08:46:04', '2025-02-14 09:10:00', 5),
    ( 96199390, 9282479,  4,   13, '223565', 1, 1, '2025-02-14 08:46:04', '2025-02-14 09:15:00', 5),
    ( 95767176, 9282479,  4,    4, '322250', 1, 8, '2025-02-10 09:03:01', '2025-02-10 09:30:00', 5),
    ( 93071882, 9282479,  4,    5, '322250', 1, 8, '2025-01-06 10:05:28', '2025-01-06 10:30:00', 5),
    ( 92150306, 9282479,  7,   14, '322205', 1,12, '2024-12-18 10:03:01', '2024-12-18 10:30:00', 5),
    ( 92151575, 9282479,  7,   15, '225124', 1,10, '2024-12-18 10:03:01', '2024-12-18 11:10:00', 5),
    ( 90538985, 9282479,  4,   13, '223565', 1, 7, '2024-11-27 16:04:28', '2024-11-27 16:40:00', 5),
    ( 90538878, 9282479,  4,    6, '322245', 1, 3, '2024-11-27 16:03:34', '2024-11-27 16:15:00', 5),
    ( 90239238, 9282479,  4,    6, '322245', 1, 8, '2024-11-25 08:36:00', '2024-11-25 09:00:00', 5),
    ( 87179588, 9282479,  4,    6, '322245', 1, 8, '2024-10-14 08:36:44', '2024-10-14 09:00:00', 5),
    ( 85760814, 9282479,  4,    6, '322245', 1, 8, '2024-09-24 09:08:50', '2024-09-24 09:30:00', 5),
    ( 85327309, 9282479,  8,   18, '322205', 1, 9, '2024-09-18 08:59:53', '2024-09-18 09:30:00', 5),
    ( 85332152, 9282479,  8,   19, '225124', 1,10, '2024-09-18 08:59:53', '2024-09-18 09:50:00', 5),
    ( 84578785, 9282479,  4,    6, '322245', 1, 3, '2024-09-09 08:53:13', '2024-09-09 09:00:00', 5),
    ( 84579670, 9282479,  4,   16, '225142', 1, 2, '2024-09-09 08:53:13', '2024-09-09 09:20:00', 5),
    ( 81793688, 9282479,  4,   13, '223565', 1, 1, '2024-08-05 08:46:01', '2024-08-05 09:15:00', 5),
    ( 79845193, 9282479,  4,    4, '322245', 1, 3, '2024-07-08 10:43:08', '2024-07-08 10:50:00', 5),
    ( 79845999, 9282479,  4,   16, '225142', 1, 2, '2024-07-08 10:43:08', '2024-07-08 11:35:00', 5),
    ( 79614642, 9282479,  9,   17, '322250', 1,11, '2024-07-04 10:26:27', '2024-07-04 13:05:00', 5);

-- Registros dos atendimentos (atendimento_prontuario)
INSERT INTO atendimento_prontuario (id, nr_atendimento, data, tipo_registro, descricao) VALUES
    -- 101677656 — UPA NOROESTE Classificação de Risco
    (1, 101677656, '2025-04-20 07:44:55.977', 1,
     '<html><head></head><body><p style="margin-bottom: 0.0; margin-top: 0"><b>Motivo Consulta: </b>PACIENTE ACOMPANHADA PELO PAIS PACIENTE RESPONSAVEL AFIRMA QUE HA 03 DIAS A PACIENTE INICIOU COM CHORO PERSISTENTE, 1 EPISODIO FEBRE 38,5C, AFIRMA DIARREIA 04 EPISODIOS SEM SANGUE, DIUSERE CONCENTRADA HA 2 DIAS, NAUSEAS, NEGA VOMITOS, GEMENTE, <b>Classificação de Risco: </b>Pouco Urgente, </p></body></html>'),
    -- 101678211 — UPA NOROESTE Atendimento Infantil Evolução
    (2, 101678211, '2025-04-20 07:54:41.517', 2,
     '<p>JA TEM UNS DIAS QUE TAVA MEIO RUIM, DEPOIS QUE SAROU FICOU GEMENDO TODA HORA. - USO DE CEFALEXINA E ATB TOPICO.</p><p>&nbsp;</p><p>ABDOME RIGIDO, CRIANCA GEMENTE, SEM ALTERACAO DE RIGIDEZ OU DE CHORO A PALPACAO.</p><p>&nbsp;</p><p>HD - DISBIOSE POS ATB.</p><p>&nbsp;</p><p>SOLICITO RADIOGRAFIA</p>'),
    -- 101678612 — UPA NOROESTE Observação Evolução
    (3, 101678612, '2025-04-20 08:18:47.601', 2,
     '<p>RX - DISTENCAO ABDOMINAL EM PONTOS ESPECIFICOS</p><p>&nbsp;</p><p>FLORA B + PREDNISOLONA</p><p>INICI</p>'),
    -- 101620758 — UPA FINSOCIAL Classificação de Risco Avaliação
    (4, 101620758, '2025-04-18 18:20:19.497', 1,
     '<html><head></head><body><p style="margin-bottom: 0.0; margin-top: 0"><b>Motivo Consulta: </b>MAE REFERE CRIANCA COM CHORO PERSISTENTE, DIARREIA LIQUIDA, FEBRE (ONTEM), NEGA ALERGIA A MEDICAMENTOS, COMORBIDADE. NO MOMENTO AFEBRIL, <b>Classificação de Risco: </b>Pouco Urgente, </p></body></html>'),
    -- 101620969 — UPA FINSOCIAL Atendimento Infantil Evolução
    (5, 101620969, '2025-04-18 18:34:18.976', 2,
     '<p># PESO: 8 KG</p><p>&nbsp;</p><p># HDA: PACIENTE COMPARECE ACOMPANHADA DA MAE, NAYANNE. RESPONSAVEL AFIRMA QUE HA 01 DIA A PACIENTE INICIOU COM CHORO PERSISTENTE E TEVE 01 EPISODIO DE FEBRE 38,5C. ALEM DISSO, AFIRMA DIARREIA 04 EPISODIOS SEM SANGUE E SEM MUCO COM INICIO HA 01 DIA. NEGA NAUSEAS, VOMITOS.</p><p>&nbsp;</p><p># HPP: NEGA COMORBIDADES / NEGA ALERGIAS.</p><p>&nbsp;</p><p># AO EXAME FISICO: BEG, AAA, CORADA E HIDRATADA / ATIVA, REATIVA E CONTACTUANTE / ABDOME: FLACIDO, NORMOTENSO, INDOLOR A PALPACAO.</p><p>&nbsp;</p><p># HD: FEBRE A/E??</p><p>&nbsp;</p><p># CD: SOLICITO HMG E EAS. REAVALIAR APOS RESULTADO.</p>'),
    -- 101620969 — UPA FINSOCIAL Exame solicitado
    (6, 101620969, '2025-04-18 18:48:38.823', 6,
     '<b><u>Exames Solicitados - UPA - EXAMES LABORATORIAIS DE URGENCIA / </b></u><br />URG - ANALISE DE CARACTERES FISICOS, ELEMENTOS E SEDIMENTO DA URINA (EAS)<br />URG - HEMOGRAMA COMPLETO'),
    -- 99418682 — UPA FINSOCIAL Classificação de Risco Avaliação
    (7, 99418682, '2025-03-24 19:31:02.434', 1,
     '<html><head></head><body><p style="margin-bottom: 0.0; margin-top: 0"><b>Motivo Consulta: </b>Mae Naiane, <b>Classificação de Risco: </b>Pouco Urgente, <b>Historico: </b>Mae relata secrecao no ouvido esquerdo, incio a aprox. 3 semanas. Em uso de pomada de neomicina e anti alergico desloratadina, sem melhora. Nega ou desconhece alergias. Nega comorbidade. Peso aprox. 8200 kg, </p></body></html>'),
    -- 99420002 — UPA FINSOCIAL Atendimento Infantil Evolução
    (8, 99420002, '2025-03-24 19:58:22.883', 2,
     '<p>VISUALIZACAO DIFICIL</p><p>HD - OTITE EXTERNA</p><p>&nbsp;</p><p>CEFALEXINA + OTOCIRIAX</p>'),
    -- 97062779 — USF BRISAS Consulta Enfermagem Evolução
    (9, 97062779, '2025-02-24 11:40:55.606', 2,
     '<p>Paciente não compareceu a consulta agendada para hoje</p>'),
    -- 97062779 — USF BRISAS Consulta Enfermagem Avaliação
    (10, 97062779, '2025-02-24 11:42:22.939', 1,
     '<html><head></head><body><p style="margin-bottom: 0.0; margin-top: 0"><b>Peso (Kg): </b>7,500, <b>Altura (cm): </b>63.0, <b>Classificação de Risco: </b>Não Urgente, </p></body></html>'),
    -- 96196958 — USF BRISAS Acolhimento Avaliação
    (11, 96196958, '2025-02-14 08:54:00', 1,
     '<html><head></head><body><p style="margin-bottom: 0.0; margin-top: 0"><b>Peso (Kg): </b>7,500, <b>Altura (cm): </b>63.0, <b>Classificação de Risco: </b>Não Urgente, </p></body></html>'),
    -- 96196827 — USF BRISAS Bolsa Familia Avaliação
    (12, 96196827, '2025-02-14 08:55:56.53', 1,
     '<html><head></head><body><p style="margin-bottom: 0.0; margin-top: 0"><b>Peso (Kg): </b>7,500, <b>Altura (cm): </b>63.0, <b>Classificação de Risco: </b>Não Urgente, <b>Observacao: </b>Acompanhamento bolsa familia, </p></body></html>'),
    -- 96199390 — USF BRISAS Consulta Enfermagem Evolução
    (13, 96199390, '2025-02-14 09:08:37.732', 2,
     '<p>Acompanhamento do bolsa familia</p>'),
    -- 92150306 — CS NORTE FERROVIARIO Acolhimento Avaliação
    (14, 92150306, '2024-12-18 10:07:30.965', 1,
     '<html><head></head><body><p style="margin-bottom: 0.0; margin-top: 0"><b>Peso (Kg): </b>7,500, <b>Altura (cm): </b>63.0, <b>Classificação de Risco: </b>Não Urgente, </p></body></html>'),
    -- 92150306 — CS NORTE FERROVIARIO Acolhimento Evolução
    (15, 92150306, '2024-12-18 10:09:45.21', 2,
     '<p>Realizado triagem para consulta</p><p>PESO .7,500</p><p>ESTATURA .63</p><p>Cd encaminhado a consulta medica</p>'),
    -- 92151575 — CS NORTE FERROVIARIO Consulta Pediatria Evolução
    (16, 92151575, '2024-12-18 11:01:46.631', 2,
     '<p>Puericultura</p><p>Corado, hidratado, acianotico, anicterico, eupneico afebril, TEC menor que 3 segundos</p><p>RCR 2t BNF sem sopro ou extrassistoles</p><p>Mvau sem ra sem esforco</p><p>Abdome flacido Rh + ausencia de vcm ou massas palpaveis</p><p>MMiis sem alteracoes</p><p>&nbsp;</p><p>cd: orientacoes, vit d neutrofer</p>'),
    -- 90538985 — USF BRISAS Bolsa Familia Avaliação
    (17, 90538985, '2024-11-27 16:29:10.373', 1,
     '<html><head></head><body><p style="margin-bottom: 0.0; margin-top: 0"><b>Peso (Kg): </b>7,600, <b>Altura (cm): </b>59.0, <b>Classificação de Risco: </b>Não Urgente, <b>Observacao: </b>Acompanhamento do bolsa familia, </p></body></html>'),
    -- 90538878 — USF BRISAS Acolhimento Avaliação
    (18, 90538878, '2024-11-27 16:07:00', 1,
     '<html><head></head><body><p style="margin-bottom: 0.0; margin-top: 0"><b>Peso (Kg): </b>7,600, <b>Classificação de Risco: </b>Não Urgente, </p></body></html>'),
    -- 85327309 — CS SETOR PERIM Acolhimento Avaliação
    (19, 85327309, '2024-09-18 09:24:02.105', 1,
     '<html><head></head><body><p style="margin-bottom: 0.0; margin-top: 0"><b>Peso (Kg): </b>5,270, <b>Altura (cm): </b>56.5, <b>Perimetro Cefalico (cm): </b>39,00, <b>Classificação de Risco: </b>Não Urgente, </p></body></html>'),
    -- 85327309 — CS SETOR PERIM Acolhimento Evolução
    (20, 85327309, '2024-09-18 09:24:48.074', 2,
     '<p>Aguardando consulta medica</p>'),
    -- 85332152 — CS SETOR PERIM Consulta Pediatria Evolução
    (21, 85332152, '2024-09-18 09:44:09.984', 2,
     '<p>rotina sem queixas suga bem eliminacoes ok gripadinha</p><p>vacina ok</p><p>lme /vit d</p><p>ao xame : sem alt</p><p>cd: orientacoes</p>'),
    -- 84578785 — USF BRISAS Acolhimento Avaliação
    (22, 84578785, '2024-09-09 08:57:00', 1,
     '<html><head></head><body><p style="margin-bottom: 0.0; margin-top: 0"><b>Peso (Kg): </b>5,500, <b>Classificação de Risco: </b>Não Urgente, </p></body></html>'),
    -- 84579670 — USF BRISAS Consulta Médica Evolução
    (23, 84579670, '2024-09-09 09:18:16.849', 2,
     '<p>Q.P.: "CONSULTA DE ROTINA"</p><p>HDA: PACIENTE ACOMPANHADA PELA MAE, COMPARECE PARA CONSULTA DE CRESCIMENTO E DESENVOLVIMENTO. NEGA QUEIXAS NO MOMENTO.</p><p>EXAME FISICO: BOM ESTADO GERAL, NORMOCORADA, HIDRATADA<br />FONTANELAS NORMOTENSAS<br />DESENVOLVIMENTO NEUROPSICOMOTOR ADEQUADO PARA IDADE</p><p>TESTES DE TRIAGEM NEONATAL SEM ALTERACOES REALIZADO DIA 12/07/2024</p><p>CD: MANTENHO MEDICAMENTOS / ENCAMINHO AO PEDIATRA (TESTE DO OLHINHO)</p>'),
    -- 81793688 — USF BRISAS Consulta Enfermagem Evolução
    (24, 81793688, '2024-08-05 09:12:23.904', 2,
     '<p>MAE TRAX LACTENTE PARA CONSULTA DE CD</p><p>MAE NEGA PATOLOGIAS INFECTOCONTAGIOSAS TRIADAS NO PRE NATAL</p><p>NASCIDO DE PARTO NORMAL / APGAR 1 9 2 9 / PC 34 CM PT 32 CM IG 38 s e 4 dias</p><p>TESTES DE TRIAGEM NEONATAL SEM ALTERACOES REALIZADO DIA 12/07/2024</p><p>Mae traz crianca para consulta de cd. nega queixas no momento / Alimentacao: LM exclusivo</p><p>PCT 37 cm / peso 4200 / est 54 cm</p><p>BOM ESTADO GERAL, NORMOCORADA, HIDRATADA</p><p>lactente com crescimento e desenvolvimento adequado para idade e sexo</p><p>conduta: orientacao</p>'),
    -- 79845193 — USF BRISAS Acolhimento Avaliação
    (25, 79845193, '2024-07-08 10:47:00', 1,
     '<html><head></head><body><p style="margin-bottom: 0.0; margin-top: 0"><b>Peso (Kg): </b>3,200, <b>Classificação de Risco: </b>Não Urgente, </p></body></html>'),
    -- 79845999 — USF BRISAS Consulta Médica Evolução
    (26, 79845999, '2024-07-08 11:29:07.929', 2,
     '<p>Q.P.: "CONSULTA DE ROTINA"</p><p>HDA: PACIENTE ACOMPANHADA PELA MAE, QUE COMPARECE PARA CONSULTA DE PUERICULTURA. NEGA QUEIXAS NO MOMENTO.</p><p>EXAME FISICO: PC: 36CM / PESO: 3,2G / COMP: 45CM</p><p>BOM ESTADO GERAL, NORMOCORADA, HIDRATADA</p><p>TESTES DE TRIAGEM NEONATAL SEM ALTERACOES</p><p>CD: 1) VITAMINA D 200UI: 2GOTAS/DIA ATE OS 2 ANOS / 2) GROW FERRO 5MG: 1 GOTA/DIA ATE OS 2 ANOS / AGUARDO RESULTADO DE TESTE DO PEZINHO / RETORNO MENSAL</p>'),
    -- 79614642 — USF ALTO DO VALE Sala Procedimentos
    (27, 79614642, '2024-07-04 13:01:18.387', 2,
     '<p>coleta teste do pezinho</p>');

