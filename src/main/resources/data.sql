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

