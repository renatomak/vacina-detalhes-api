-- 1. CRIAÇÃO DAS TABELAS
CREATE TABLE IF NOT EXISTS estado (
                                      cod_est INTEGER PRIMARY KEY,
                                      sigla VARCHAR(2) NOT NULL
    );

CREATE TABLE IF NOT EXISTS cidade (
                                      cod_cid INTEGER PRIMARY KEY,
                                      descricao VARCHAR(100) NOT NULL,
    cod_est INTEGER NOT NULL,
    FOREIGN KEY (cod_est) REFERENCES estado(cod_est)
    );

CREATE TABLE IF NOT EXISTS tipo_logradouro_cadsus (
                                                      cd_tipo_logradouro INTEGER PRIMARY KEY,
                                                      ds_tipo_logradouro VARCHAR(50) NOT NULL
    );

CREATE TABLE IF NOT EXISTS endereco_usuario_cadsus (
                                                       cd_endereco INTEGER PRIMARY KEY,
                                                       keyword VARCHAR(10),
    nm_logradouro VARCHAR(100),
    nm_comp_logradouro VARCHAR(50),
    nr_logradouro VARCHAR(10),
    cep VARCHAR(10),
    nm_bairro VARCHAR(50),
    cod_cid INTEGER NOT NULL,
    cd_tipo_logradouro INTEGER NOT NULL,
    FOREIGN KEY (cod_cid) REFERENCES cidade(cod_cid),
    FOREIGN KEY (cd_tipo_logradouro) REFERENCES tipo_logradouro_cadsus(cd_tipo_logradouro)
    );

CREATE TABLE IF NOT EXISTS usuario_cadsus (
                                              cd_usu_cadsus INTEGER PRIMARY KEY,
                                              nm_usuario VARCHAR(100) NOT NULL,
    cpf VARCHAR(20) NOT NULL,
    sg_sexo CHAR(1),
    nm_mae VARCHAR(100),
    nm_pai VARCHAR(100),
    dt_nascimento DATE,
    nr_telefone VARCHAR(20),
    cd_endereco INTEGER NOT NULL,
    FOREIGN KEY (cd_endereco) REFERENCES endereco_usuario_cadsus(cd_endereco)
    );

-- Script H2 para testes do prontuário eletrônico
-- Tabelas principais

CREATE TABLE IF NOT EXISTS empresa (
    empresa INT PRIMARY KEY,
    descricao VARCHAR(60),
    telefone VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS classificacao_risco (
    cd_classificacao_risco BIGINT PRIMARY KEY,
    descricao VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS classificacao_atendimento (
    cd_cla_atendimento INT PRIMARY KEY,
    ds_cla_atendimento VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS tabela_cbo (
    cd_cbo VARCHAR(10) PRIMARY KEY,
    ds_cbo VARCHAR(150)
);

CREATE TABLE IF NOT EXISTS orgao_emissor (
    cd_orgao_emissor INT PRIMARY KEY,
    sg_orgao_emissor VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS profissional (
    cd_profissional INT PRIMARY KEY,
    nm_profissional VARCHAR(60),
    nr_registro VARCHAR(50),
    cd_con_classe INT,
    cd_cbo VARCHAR(10),
    FOREIGN KEY (cd_con_classe) REFERENCES orgao_emissor(cd_orgao_emissor),
    FOREIGN KEY (cd_cbo) REFERENCES tabela_cbo(cd_cbo)
);

CREATE TABLE IF NOT EXISTS tipo_atendimento (
    cd_tp_atendimento BIGINT PRIMARY KEY,
    ds_tipo_atendimento VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS natureza_procura_tp_atendimento (
    cd_nat_proc_tp_atendimento BIGINT PRIMARY KEY,
    cd_tp_atendimento BIGINT,
    FOREIGN KEY (cd_tp_atendimento) REFERENCES tipo_atendimento(cd_tp_atendimento)
);

CREATE TABLE IF NOT EXISTS cid (
    cd_cid BIGINT PRIMARY KEY,
    ds_cid VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS atendimento (
    nr_atendimento BIGINT PRIMARY KEY,
    empresa INT,
    cd_usu_cadsus BIGINT,
    dt_chegada TIMESTAMP,
    dt_fechamento TIMESTAMP,
    dt_alta TIMESTAMP,
    status INT,
    classificacao_risco BIGINT,
    cd_cla_atendimento INT,
    cd_cid_principal BIGINT,
    cd_cid_secundario BIGINT,
    cd_nat_proc_tp_atendimento BIGINT,
    motivo_consulta VARCHAR(1000),
    cd_profissional INT,
    cd_profissional_responsavel INT,
    cd_cbo VARCHAR(10),
    dt_atendimento TIMESTAMP,
    FOREIGN KEY (empresa) REFERENCES empresa(empresa),
    FOREIGN KEY (classificacao_risco) REFERENCES classificacao_risco(cd_classificacao_risco),
    FOREIGN KEY (cd_cla_atendimento) REFERENCES classificacao_atendimento(cd_cla_atendimento),
    FOREIGN KEY (cd_cid_principal) REFERENCES cid(cd_cid),
    FOREIGN KEY (cd_cid_secundario) REFERENCES cid(cd_cid),
    FOREIGN KEY (cd_nat_proc_tp_atendimento) REFERENCES natureza_procura_tp_atendimento(cd_nat_proc_tp_atendimento),
    FOREIGN KEY (cd_profissional) REFERENCES profissional(cd_profissional),
    FOREIGN KEY (cd_profissional_responsavel) REFERENCES profissional(cd_profissional),
    FOREIGN KEY (cd_cbo) REFERENCES tabela_cbo(cd_cbo)
);

CREATE TABLE IF NOT EXISTS evolucao_prontuario (
    cd_evolucao_prontuario BIGINT PRIMARY KEY,
    nr_atendimento BIGINT,
    dt_historico TIMESTAMP,
    cd_profissional INT,
    ds_prontuario TEXT,
    cd_cbo VARCHAR(10),
    tipo_registro BIGINT,
    FOREIGN KEY (nr_atendimento) REFERENCES atendimento(nr_atendimento),
    FOREIGN KEY (cd_profissional) REFERENCES profissional(cd_profissional),
    FOREIGN KEY (cd_cbo) REFERENCES tabela_cbo(cd_cbo)
);

CREATE TABLE IF NOT EXISTS atendimento_primario (
    cd_atendimento_primario BIGINT PRIMARY KEY,
    nr_atendimento BIGINT,
    dt_avaliacao TIMESTAMP,
    peso DECIMAL(12,3),
    altura DECIMAL(12,2),
    perimetro_cefalico DECIMAL(12,2),
    historico VARCHAR(1000),
    motivo_consulta VARCHAR(1000),
    cd_evolucao_prontuario BIGINT,
    FOREIGN KEY (nr_atendimento) REFERENCES atendimento(nr_atendimento),
    FOREIGN KEY (cd_evolucao_prontuario) REFERENCES evolucao_prontuario(cd_evolucao_prontuario)
);


-- 2. LIMPEZA DE DADOS
DELETE FROM usuario_cadsus;
DELETE FROM endereco_usuario_cadsus;
DELETE FROM tipo_logradouro_cadsus;
DELETE FROM cidade;
DELETE FROM estado;
DELETE FROM atendimento_primario;
DELETE FROM evolucao_prontuario;
DELETE FROM atendimento;
DELETE FROM cid;
DELETE FROM profissional;
DELETE FROM orgao_emissor;
DELETE FROM tabela_cbo;
DELETE FROM natureza_procura_tp_atendimento;
DELETE FROM tipo_atendimento;
DELETE FROM classificacao_atendimento;
DELETE FROM classificacao_risco;
DELETE FROM empresa;

-- 3. INSERÇÃO DOS DADOS (BASEADO NO TXT)
INSERT INTO estado (cod_est, sigla) VALUES (1, 'GO');

INSERT INTO cidade (cod_cid, descricao, cod_est) VALUES (151, 'GOIANIA', 1);

INSERT INTO tipo_logradouro_cadsus (cd_tipo_logradouro, ds_tipo_logradouro) VALUES (81, 'RUA');

INSERT INTO endereco_usuario_cadsus (
    cd_endereco, keyword, nm_logradouro, nm_comp_logradouro, nr_logradouro, cep, nm_bairro, cod_cid, cd_tipo_logradouro
) VALUES (
             7230412,
             'K1',
             'C 149',
             'QD 325 LT 11',
             'SN',
             '74230050',
             'JARDIM AMERICA',
             151,
             81
         );

INSERT INTO usuario_cadsus (
    cd_usu_cadsus, nm_usuario, cpf, sg_sexo, nm_mae, nm_pai, dt_nascimento, nr_telefone, cd_endereco
) VALUES (
             3033700,
             'MARIA EDUARDA HUMMEL OLIVEIRA',
             '05342621180',
             'F',
             'ANA PAULA HUMMEL OLIVEIRA',
             'ADRIANO DE OLIVEIRA',
             '2006-05-18',
             '62991632742',
             7230412
         );

-- Inserts de exemplo para H2 extraídos do prontuarioResponse.json

-- Empresa (unidade)
INSERT INTO empresa (empresa, descricao, telefone) VALUES (1, 'UPA MARIA PIRES PERILLO - UPA NOROESTE', '(62) 3524-3461');
INSERT INTO empresa (empresa, descricao, telefone) VALUES (2, 'USF / APS', NULL);

-- Classificação de risco
INSERT INTO classificacao_risco (cd_classificacao_risco, descricao) VALUES (1, 'Classificação de Risco Exemplo');

-- Classificação de atendimento
INSERT INTO classificacao_atendimento (cd_cla_atendimento, ds_cla_atendimento) VALUES (1, 'UPA - CLASSIFICAÇÃO DE RISCO');
INSERT INTO classificacao_atendimento (cd_cla_atendimento, ds_cla_atendimento) VALUES (2, 'APS - CONSULTA DE ENFERMAGEM');
INSERT INTO classificacao_atendimento (cd_cla_atendimento, ds_cla_atendimento) VALUES (3, 'USF - ACOLHIMENTO À DEMANDA ESPONTÂNEA');
INSERT INTO classificacao_atendimento (cd_cla_atendimento, ds_cla_atendimento) VALUES (4, 'APS - ACOLHIMENTO');
INSERT INTO classificacao_atendimento (cd_cla_atendimento, ds_cla_atendimento) VALUES (5, 'APS - SALA DE PROCEDIMENTOS');

-- Tipo de atendimento
INSERT INTO tipo_atendimento (cd_tp_atendimento, ds_tipo_atendimento) VALUES (1, 'UPA - CLASSIFICAÇÃO DE RISCO');
INSERT INTO tipo_atendimento (cd_tp_atendimento, ds_tipo_atendimento) VALUES (2, 'APS - CONSULTA DE ENFERMAGEM');
INSERT INTO tipo_atendimento (cd_tp_atendimento, ds_tipo_atendimento) VALUES (3, 'USF - ACOLHIMENTO À DEMANDA ESPONTÂNEA');
INSERT INTO tipo_atendimento (cd_tp_atendimento, ds_tipo_atendimento) VALUES (4, 'APS - ACOLHIMENTO');
INSERT INTO tipo_atendimento (cd_tp_atendimento, ds_tipo_atendimento) VALUES (5, 'APS - SALA DE PROCEDIMENTOS');

-- Natureza procura tipo atendimento
INSERT INTO natureza_procura_tp_atendimento (cd_nat_proc_tp_atendimento, cd_tp_atendimento) VALUES (1, 1);
INSERT INTO natureza_procura_tp_atendimento (cd_nat_proc_tp_atendimento, cd_tp_atendimento) VALUES (2, 2);
INSERT INTO natureza_procura_tp_atendimento (cd_nat_proc_tp_atendimento, cd_tp_atendimento) VALUES (3, 3);
INSERT INTO natureza_procura_tp_atendimento (cd_nat_proc_tp_atendimento, cd_tp_atendimento) VALUES (4, 4);
INSERT INTO natureza_procura_tp_atendimento (cd_nat_proc_tp_atendimento, cd_tp_atendimento) VALUES (5, 5);

-- Orgao emissor
INSERT INTO orgao_emissor (cd_orgao_emissor, sg_orgao_emissor) VALUES (1, 'COREN');

-- Tabela CBO
INSERT INTO tabela_cbo (cd_cbo, ds_cbo) VALUES ('2235', 'Enfermeiro');

-- Profissional
INSERT INTO profissional (cd_profissional, nm_profissional, nr_registro, cd_con_classe, cd_cbo) VALUES (1, 'ENFERMEIRO EXEMPLO', '123456', 1, '2235');

-- CID
INSERT INTO cid (cd_cid, ds_cid) VALUES (1, 'CID Exemplo');

-- Atendimento (exemplo de um atendimento)
INSERT INTO atendimento (nr_atendimento, empresa, cd_usu_cadsus, dt_chegada, dt_fechamento, dt_alta, status, classificacao_risco, cd_cla_atendimento, cd_cid_principal, cd_cid_secundario, cd_nat_proc_tp_atendimento, motivo_consulta, cd_profissional, cd_profissional_responsavel, cd_cbo, dt_atendimento) VALUES (101677656, 1, 9282479, PARSEDATETIME('20/04/2025 07:12:54', 'dd/MM/yyyy HH:mm:ss'), NULL, NULL, 1, 1, 1, 1, NULL, 1, 'Motivo exemplo', 1, 1, '2235', PARSEDATETIME('20/04/2025 07:44:55', 'dd/MM/yyyy HH:mm:ss'));

-- Evolução Prontuário (exemplo)
INSERT INTO evolucao_prontuario (cd_evolucao_prontuario, nr_atendimento, dt_historico, cd_profissional, ds_prontuario, cd_cbo, tipo_registro) VALUES (1, 101677656, PARSEDATETIME('20/04/2025 07:44:55', 'dd/MM/yyyy HH:mm:ss'), 1, 'Evolução exemplo', '2235', 1);

-- Atendimento Primário (exemplo)
INSERT INTO atendimento_primario (cd_atendimento_primario, nr_atendimento, dt_avaliacao, peso, altura, perimetro_cefalico, historico, motivo_consulta, cd_evolucao_prontuario) VALUES (1, 101677656, PARSEDATETIME('20/04/2025 07:44:55', 'dd/MM/yyyy HH:mm:ss'), 10.5, 80.0, 45.0, 'Histórico exemplo', 'Motivo exemplo', 1);

