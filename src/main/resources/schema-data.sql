-- Criação de tabelas e dados para ambiente local (H2 ou PostgreSQL)

CREATE TABLE estado (
    cod_est INTEGER PRIMARY KEY,
    sigla VARCHAR(2) NOT NULL
);

CREATE TABLE cidade (
    cod_cid INTEGER PRIMARY KEY,
    descricao VARCHAR(100) NOT NULL,
    cod_est INTEGER NOT NULL,
    FOREIGN KEY (cod_est) REFERENCES estado(cod_est)
);

CREATE TABLE tipo_logradouro_cadsus (
    cd_tipo_logradouro INTEGER PRIMARY KEY,
    ds_tipo_logradouro VARCHAR(50) NOT NULL
);

CREATE TABLE endereco_usuario_cadsus (
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

CREATE TABLE usuario_cadsus (
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

-- Dados para testes locais

INSERT INTO estado (cod_est, sigla) VALUES (1, 'GO');
INSERT INTO cidade (cod_cid, descricao, cod_est) VALUES (10, 'Goiania', 1);
INSERT INTO tipo_logradouro_cadsus (cd_tipo_logradouro, ds_tipo_logradouro) VALUES (100, 'Rua');
INSERT INTO endereco_usuario_cadsus (
    cd_endereco, keyword, nm_logradouro, nm_comp_logradouro, nr_logradouro, cep, nm_bairro, cod_cid, cd_tipo_logradouro
) VALUES
    (1000, 'K1', 'Rua A', 'Ap 1', '10', '74000000', 'Centro', 10, 100),
    (1001, 'K2', 'Rua B', 'Casa', '20', '74000001', 'Setor Sul', 10, 100);

INSERT INTO usuario_cadsus (
    cd_usu_cadsus, nm_usuario, cpf, sg_sexo, nm_mae, nm_pai, dt_nascimento, nr_telefone, cd_endereco
) VALUES
    (3033700, 'MARIA EDUARDA HUMMEL OLIVEIRA', '12345678901', 'F', 'ANA', 'JOAO', '1990-01-01', '62999999999', 1000),
    (7228663, 'JOAO SILVA', '98765432100', 'M', 'MARIA', 'JOSE', '1985-05-10', '62988888888', 1001);

