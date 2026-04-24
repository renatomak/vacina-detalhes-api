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

-- 2. LIMPEZA DE DADOS
DELETE FROM usuario_cadsus;
DELETE FROM endereco_usuario_cadsus;
DELETE FROM tipo_logradouro_cadsus;
DELETE FROM cidade;
DELETE FROM estado;

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
