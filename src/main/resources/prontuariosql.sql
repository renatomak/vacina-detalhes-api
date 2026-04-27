-- =============================================================================
-- QUERY PRONTUÁRIO ELETRÔNICO - REGISTROS POR PACIENTE
-- =============================================================================
-- Estratégia: UNION ALL de 4 "fontes de registro" para evitar produto cartesiano
-- entre evolucao_prontuario e atendimento_primario.
--
-- Fonte 1 → Avaliação (atendimento_primario com dt_avaliacao preenchida)
-- Fonte 2 → Evolução (evolucao_prontuario - texto clínico)
-- Fonte 3 → Atendimento (registro-cabeçalho do atendimento - CID principal)
-- Fonte 4 → Exames (pedido_exame / atendimento_exame - se existir a tabela)
-- Fonte 5 → Procedimentos (atendimento_procedimento - se existir a tabela)
--
-- Os dados de cabeçalho do atendimento (unidade, chegada, etc.) são repetidos
-- em cada linha mas sem JOIN cruzado - cada fonte já carrega o nr_atendimento.
-- =============================================================================

WITH base_atendimento AS (
    -- CTE com dados fixos de cabeçalho de cada atendimento do paciente
    -- Evita repetir os mesmos JOINs pesados em cada ramo do UNION
    SELECT
        a.nr_atendimento,
        a.empresa,
        a.cd_usu_cadsus,
        a.dt_chegada,
        a.dt_fechamento,
        a.dt_alta,
        a.status,
        a.classificacao_risco,
        a.cd_cla_atendimento,
        a.cd_cid_principal,
        a.cd_cid_secundario,
        a.cd_nat_proc_tp_atendimento,
        a.motivo_consulta                         AS motivo_consulta_atendimento,

        -- Unidade / Empresa
        e.descricao                               AS nm_unidade,
        e.telefone                                AS tel_unidade,

        -- Tipo de atendimento (via natureza_procura_tp_atendimento → tipo_atendimento)
        ta.ds_tipo_atendimento                    AS ds_tipo_atendimento,

        -- Classificação de risco do atendimento
        cr.descricao                              AS ds_classificacao_risco,

        -- Tempo de permanência em horas:minutos (apenas quando fechamento existe)
        CASE
            WHEN a.dt_fechamento IS NOT NULL
            THEN TO_CHAR(
                    EXTRACT(EPOCH FROM (a.dt_fechamento - a.dt_chegada)) / 3600,
                    'FM9999'
                 ) || ':' ||
                 LPAD(
                    (EXTRACT(EPOCH FROM (a.dt_fechamento - a.dt_chegada))::int % 3600 / 60)::text,
                    2, '0'
                 )
            ELSE NULL
        END                                       AS tempo_permanencia

    FROM atendimento a
    INNER JOIN empresa e
        ON e.empresa = a.empresa
    LEFT JOIN natureza_procura_tp_atendimento npta
        ON npta.cd_nat_proc_tp_atendimento = a.cd_nat_proc_tp_atendimento
    LEFT JOIN tipo_atendimento ta
        ON ta.cd_tp_atendimento = npta.cd_tp_atendimento
    LEFT JOIN classificacao_risco cr
        ON cr.cd_classificacao_risco = a.classificacao_risco
    WHERE
        a.cd_usu_cadsus = :pacienteId
        AND a.status <> 2   -- exclui cancelados
)

-- =============================================================================
-- FONTE 1: AVALIAÇÕES (triagem / classificação de risco / acolhimento)
-- Origem: atendimento_primario
-- Tipo de registro: 'Avaliação'
-- Profissional: o do atendimento_primario → vinculado via cd_evolucao_prontuario
--               ou, quando não há vínculo, usa o profissional do atendimento
-- =============================================================================
SELECT
    ba.nr_atendimento,
    ba.nm_unidade,
    ba.tel_unidade,
    TO_CHAR(ba.dt_chegada,   'DD/MM/YYYY HH24:MI:SS')  AS chegada,
    TO_CHAR(ba.dt_fechamento,'DD/MM/YYYY HH24:MI:SS')  AS saida,
    ba.tempo_permanencia,
    ba.ds_tipo_atendimento                              AS tipo_atendimento_cabecalho,

    -- Data do REGISTRO (usa dt_avaliacao do atendimento_primario)
    TO_CHAR(ap.dt_avaliacao, 'DD/MM/YYYY HH24:MI:SS')  AS data_registro,
    ap.dt_avaliacao                                     AS dt_registro_order,  -- para ordenação

    -- Tipo lógico de registro
    'Avaliação'                                         AS tipo_registro,

    -- Tipo de atendimento DO REGISTRO (mesmo do atendimento pai, pode divergir)
    ba.ds_tipo_atendimento                              AS tipo_atendimento_registro,

    -- Profissional que realizou a avaliação
    -- Tenta via evolucao_prontuario linkada ao atendimento_primario
    COALESCE(p_ep.nm_profissional, p_ate.nm_profissional)   AS prof_nome,
    COALESCE(p_ep.nr_registro,     p_ate.nr_registro)       AS prof_registro,
    COALESCE(oe_ep.sg_orgao_emissor, oe_ate.sg_orgao_emissor) AS prof_tipo_registro,
    COALESCE(tc_ep.cd_cbo,  tc_ate.cd_cbo)                  AS prof_cbo,
    COALESCE(tc_ep.ds_cbo,  tc_ate.ds_cbo)                  AS prof_cbo_descricao,

    -- Conteúdo da Avaliação
    COALESCE(ap.motivo_consulta, ba.motivo_consulta_atendimento) AS motivo_consulta,
    ba.ds_classificacao_risco                           AS classificacao_risco,
    ap.peso                                             AS peso,
    ap.altura                                           AS altura,
    ap.perimetro_cefalico                               AS perimetro_cefalico,
    ap.historico                                        AS historico,
    NULL::text                                          AS evolucao,
    NULL::text                                          AS cid_codigo,
    NULL::text                                          AS cid_descricao,
    NULL::text                                          AS exame_grupo,
    NULL::text[]                                        AS exame_itens,
    NULL::text[]                                        AS procedimentos,
    NULL::text                                          AS subtitulo,
    NULL::text                                          AS observacao

FROM base_atendimento ba
INNER JOIN atendimento_primario ap
    ON ap.nr_atendimento = ba.nr_atendimento

-- Tenta buscar o profissional via evolucao_prontuario vinculada
LEFT JOIN evolucao_prontuario ep_link
    ON ep_link.cd_evolucao_prontuario = ap.cd_evolucao_prontuario

LEFT JOIN profissional p_ep
    ON p_ep.cd_profissional = ep_link.cd_profissional::int4
LEFT JOIN tabela_cbo tc_ep
    ON tc_ep.cd_cbo = ep_link.cd_cbo
LEFT JOIN orgao_emissor oe_ep
    ON oe_ep.cd_orgao_emissor = p_ep.cd_con_classe

-- Fallback: profissional do atendimento pai
LEFT JOIN atendimento a_orig
    ON a_orig.nr_atendimento = ba.nr_atendimento
LEFT JOIN profissional p_ate
    ON p_ate.cd_profissional = a_orig.cd_profissional
LEFT JOIN tabela_cbo tc_ate
    ON tc_ate.cd_cbo = a_orig.cd_cbo
LEFT JOIN orgao_emissor oe_ate
    ON oe_ate.cd_orgao_emissor = p_ate.cd_con_classe

WHERE ap.dt_avaliacao IS NOT NULL

UNION ALL

-- =============================================================================
-- FONTE 2: EVOLUÇÕES CLÍNICAS (texto médico / de enfermagem)
-- Origem: evolucao_prontuario
-- Tipo de registro: 'Evolução'
-- Profissional: o da evolucao_prontuario (quem escreveu)
-- Tipo de atendimento do REGISTRO: via cd_cbo da evolução → lookup no tipo_atendimento
--   (se não disponível, usa o do atendimento pai)
-- =============================================================================
SELECT
    ba.nr_atendimento,
    ba.nm_unidade,
    ba.tel_unidade,
    TO_CHAR(ba.dt_chegada,   'DD/MM/YYYY HH24:MI:SS')  AS chegada,
    TO_CHAR(ba.dt_fechamento,'DD/MM/YYYY HH24:MI:SS')  AS saida,
    ba.tempo_permanencia,
    ba.ds_tipo_atendimento                              AS tipo_atendimento_cabecalho,

    TO_CHAR(ep.dt_historico, 'DD/MM/YYYY HH24:MI:SS')  AS data_registro,
    ep.dt_historico                                     AS dt_registro_order,

    'Evolução'                                          AS tipo_registro,

    -- Tipo de atendimento específico da evolução (pode ser diferente do pai)
    -- Ex: "UPA - ATENDIMENTO INFANTIL" vs "UPA - CLASSIFICAÇÃO DE RISCO"
    COALESCE(ta_ep.ds_tipo_atendimento, ba.ds_tipo_atendimento) AS tipo_atendimento_registro,

    p.nm_profissional                                   AS prof_nome,
    p.nr_registro                                       AS prof_registro,
    oe.sg_orgao_emissor                                 AS prof_tipo_registro,
    tc.cd_cbo                                           AS prof_cbo,
    tc.ds_cbo                                           AS prof_cbo_descricao,

    NULL::text                                          AS motivo_consulta,
    NULL::text                                          AS classificacao_risco,
    NULL::numeric                                       AS peso,
    NULL::numeric                                       AS altura,
    NULL::numeric                                       AS perimetro_cefalico,
    NULL::text                                          AS historico,
    ep.ds_prontuario                                    AS evolucao,
    NULL::text                                          AS cid_codigo,
    NULL::text                                          AS cid_descricao,
    NULL::text                                          AS exame_grupo,
    NULL::text[]                                        AS exame_itens,
    NULL::text[]                                        AS procedimentos,
    NULL::text                                          AS subtitulo,
    NULL::text                                          AS observacao

FROM base_atendimento ba
INNER JOIN evolucao_prontuario ep
    ON ep.nr_atendimento = ba.nr_atendimento

-- Profissional da evolução
LEFT JOIN profissional p
    ON p.cd_profissional = ep.cd_profissional::int4
LEFT JOIN tabela_cbo tc
    ON tc.cd_cbo = ep.cd_cbo
LEFT JOIN orgao_emissor oe
    ON oe.cd_orgao_emissor = p.cd_con_classe

-- Tipo de atendimento inferido pelo CBO da evolução
-- (Join auxiliar - pode ser NULL se não houver vínculo direto)
LEFT JOIN tipo_atendimento ta_ep
    ON ta_ep.cd_tp_atendimento = ep.tipo_registro::int8  -- se tipo_registro mapear para cd_tp_atendimento

WHERE ep.ds_prontuario IS NOT NULL
  AND ep.ds_prontuario <> ''
  -- Exclui evoluções que são apenas "capa" do atendimento_primario (já capturadas na Fonte 1)
  AND NOT EXISTS (
      SELECT 1
      FROM atendimento_primario ap2
      WHERE ap2.cd_evolucao_prontuario = ep.cd_evolucao_prontuario
        AND ap2.nr_atendimento = ba.nr_atendimento
  )

UNION ALL

-- =============================================================================
-- FONTE 3: REGISTRO DE ATENDIMENTO (dados clínicos finais: CID, conduta)
-- Origem: atendimento (campos cd_cid_principal, cd_cid_secundario)
-- Tipo de registro: 'Atendimento'
-- Representa o "fechamento" médico com diagnóstico
-- Profissional: o responsável do atendimento
-- =============================================================================
SELECT
    ba.nr_atendimento,
    ba.nm_unidade,
    ba.tel_unidade,
    TO_CHAR(ba.dt_chegada,   'DD/MM/YYYY HH24:MI:SS')  AS chegada,
    TO_CHAR(ba.dt_fechamento,'DD/MM/YYYY HH24:MI:SS')  AS saida,
    ba.tempo_permanencia,
    ba.ds_tipo_atendimento                              AS tipo_atendimento_cabecalho,

    -- Data do registro = dt_atendimento (quando o médico fechou o atendimento)
    TO_CHAR(a.dt_atendimento,'DD/MM/YYYY HH24:MI:SS')  AS data_registro,
    a.dt_atendimento                                   AS dt_registro_order,

    'Atendimento'                                       AS tipo_registro,

    ba.ds_tipo_atendimento                              AS tipo_atendimento_registro,

    p.nm_profissional                                   AS prof_nome,
    p.nr_registro                                       AS prof_registro,
    oe.sg_orgao_emissor                                 AS prof_tipo_registro,
    tc.cd_cbo                                           AS prof_cbo,
    tc.ds_cbo                                           AS prof_cbo_descricao,

    a.motivo_consulta                                   AS motivo_consulta,
    NULL::text                                          AS classificacao_risco,
    NULL::numeric                                       AS peso,
    NULL::numeric                                       AS altura,
    NULL::numeric                                       AS perimetro_cefalico,
    NULL::text                                          AS historico,
    NULL::text                                          AS evolucao,

    -- CID principal (código e descrição via tabela cid)
    a.cd_cid_principal                                  AS cid_codigo,
    cid.ds_cid                                          AS cid_descricao,

    NULL::text                                          AS exame_grupo,
    NULL::text[]                                        AS exame_itens,
    NULL::text[]                                        AS procedimentos,
    NULL::text                                          AS subtitulo,
    NULL::text                                          AS observacao

FROM base_atendimento ba
INNER JOIN atendimento a
    ON a.nr_atendimento = ba.nr_atendimento

-- Profissional responsável (cd_profissional_responsavel tem prioridade, senão cd_profissional)
LEFT JOIN profissional p
    ON p.cd_profissional = COALESCE(a.cd_profissional_responsavel, a.cd_profissional)
LEFT JOIN tabela_cbo tc
    ON tc.cd_cbo = a.cd_cbo
LEFT JOIN orgao_emissor oe
    ON oe.cd_orgao_emissor = p.cd_con_classe

-- CID principal
LEFT JOIN cid
    ON cid.cd_cid = a.cd_cid_principal

WHERE a.cd_cid_principal IS NOT NULL   -- só gera linha de 'Atendimento' se houver CID
  AND a.dt_atendimento IS NOT NULL

UNION ALL

-- =============================================================================
-- FONTE 4: EXAMES SOLICITADOS
-- Origem: pedido_exame + pedido_exame_item (ajuste os nomes conforme seu schema)
-- Tipo de registro: 'Exame'
-- ATENÇÃO: Descomente e ajuste os nomes das tabelas conforme existirem no seu banco.
-- =============================================================================
/*
SELECT
    ba.nr_atendimento,
    ba.nm_unidade,
    ba.tel_unidade,
    TO_CHAR(ba.dt_chegada,   'DD/MM/YYYY HH24:MI:SS')  AS chegada,
    TO_CHAR(ba.dt_fechamento,'DD/MM/YYYY HH24:MI:SS')  AS saida,
    ba.tempo_permanencia,
    ba.ds_tipo_atendimento                              AS tipo_atendimento_cabecalho,

    TO_CHAR(pe.dt_pedido,    'DD/MM/YYYY HH24:MI:SS')  AS data_registro,
    pe.dt_pedido                                        AS dt_registro_order,

    'Exame'                                             AS tipo_registro,
    ba.ds_tipo_atendimento                              AS tipo_atendimento_registro,

    p.nm_profissional                                   AS prof_nome,
    p.nr_registro                                       AS prof_registro,
    oe.sg_orgao_emissor                                 AS prof_tipo_registro,
    tc.cd_cbo                                           AS prof_cbo,
    tc.ds_cbo                                           AS prof_cbo_descricao,

    NULL::text                                          AS motivo_consulta,
    NULL::text                                          AS classificacao_risco,
    NULL::numeric                                       AS peso,
    NULL::numeric                                       AS altura,
    NULL::numeric                                       AS perimetro_cefalico,
    NULL::text                                          AS historico,
    NULL::text                                          AS evolucao,
    NULL::text                                          AS cid_codigo,
    NULL::text                                          AS cid_descricao,

    tge.ds_tipo_grupo_exame                             AS exame_grupo,
    ARRAY_AGG(te.ds_tipo_exame ORDER BY pei.seq)        AS exame_itens,

    NULL::text[]                                        AS procedimentos,
    NULL::text                                          AS subtitulo,
    NULL::text                                          AS observacao

FROM base_atendimento ba
INNER JOIN pedido_exame pe                 -- ajuste nome da tabela
    ON pe.nr_atendimento = ba.nr_atendimento
INNER JOIN pedido_exame_item pei           -- ajuste nome da tabela
    ON pei.cd_pedido_exame = pe.cd_pedido_exame
LEFT JOIN tipo_exame te                    -- ajuste nome da tabela
    ON te.cd_tipo_exame = pei.cd_tipo_exame
LEFT JOIN tipo_grupo_exame tge             -- ajuste nome da tabela
    ON tge.cd_tipo_grupo_exame = te.cd_tipo_grupo_exame
LEFT JOIN profissional p
    ON p.cd_profissional = pe.cd_profissional
LEFT JOIN tabela_cbo tc
    ON tc.cd_cbo = pe.cd_cbo
LEFT JOIN orgao_emissor oe
    ON oe.cd_orgao_emissor = p.cd_con_classe
GROUP BY
    ba.nr_atendimento, ba.nm_unidade, ba.tel_unidade,
    ba.dt_chegada, ba.dt_fechamento, ba.tempo_permanencia, ba.ds_tipo_atendimento,
    pe.dt_pedido, pe.cd_pedido_exame,
    p.nm_profissional, p.nr_registro, oe.sg_orgao_emissor, tc.cd_cbo, tc.ds_cbo,
    tge.ds_tipo_grupo_exame
*/

UNION ALL

-- =============================================================================
-- FONTE 5: PROCEDIMENTOS REALIZADOS
-- Origem: atendimento_procedimento (ajuste o nome conforme seu schema)
-- Tipo de registro: 'Procedimentos'
-- ATENÇÃO: Descomente e ajuste os nomes das tabelas conforme existirem no seu banco.
-- =============================================================================
/*
SELECT
    ba.nr_atendimento,
    ba.nm_unidade,
    ba.tel_unidade,
    TO_CHAR(ba.dt_chegada,   'DD/MM/YYYY HH24:MI:SS')  AS chegada,
    TO_CHAR(ba.dt_fechamento,'DD/MM/YYYY HH24:MI:SS')  AS saida,
    ba.tempo_permanencia,
    ba.ds_tipo_atendimento                              AS tipo_atendimento_cabecalho,

    TO_CHAR(aprc.dt_procedimento, 'DD/MM/YYYY HH24:MI:SS') AS data_registro,
    aprc.dt_procedimento                                AS dt_registro_order,

    'Procedimentos'                                     AS tipo_registro,
    ba.ds_tipo_atendimento                              AS tipo_atendimento_registro,

    p.nm_profissional                                   AS prof_nome,
    p.nr_registro                                       AS prof_registro,
    oe.sg_orgao_emissor                                 AS prof_tipo_registro,
    tc.cd_cbo                                           AS prof_cbo,
    tc.ds_cbo                                           AS prof_cbo_descricao,

    NULL::text                                          AS motivo_consulta,
    NULL::text                                          AS classificacao_risco,
    NULL::numeric                                       AS peso,
    NULL::numeric                                       AS altura,
    NULL::numeric                                       AS perimetro_cefalico,
    NULL::text                                          AS historico,
    NULL::text                                          AS evolucao,
    NULL::text                                          AS cid_codigo,
    NULL::text                                          AS cid_descricao,
    NULL::text                                          AS exame_grupo,
    NULL::text[]                                        AS exame_itens,

    ARRAY_AGG(proc.ds_procedimento ORDER BY aprc.seq)   AS procedimentos,

    NULL::text                                          AS subtitulo,
    NULL::text                                          AS observacao

FROM base_atendimento ba
INNER JOIN atendimento_procedimento aprc   -- ajuste nome da tabela
    ON aprc.nr_atendimento = ba.nr_atendimento
LEFT JOIN procedimento proc                -- ajuste nome da tabela
    ON proc.cd_procedimento = aprc.cd_procedimento
LEFT JOIN profissional p
    ON p.cd_profissional = aprc.cd_profissional
LEFT JOIN tabela_cbo tc
    ON tc.cd_cbo = aprc.cd_cbo
LEFT JOIN orgao_emissor oe
    ON oe.cd_orgao_emissor = p.cd_con_classe
GROUP BY
    ba.nr_atendimento, ba.nm_unidade, ba.tel_unidade,
    ba.dt_chegada, ba.dt_fechamento, ba.tempo_permanencia, ba.ds_tipo_atendimento,
    aprc.dt_procedimento,
    p.nm_profissional, p.nr_registro, oe.sg_orgao_emissor, tc.cd_cbo, tc.ds_cbo
*/

-- =============================================================================
-- ORDENAÇÃO FINAL
-- Atendimentos: do mais recente para o mais antigo (dt_chegada DESC)
-- Registros internos: ordem cronológica de inserção (dt_registro_order ASC)
-- =============================================================================
ORDER BY
    dt_chegada DESC,          -- substitua por ba.dt_chegada se usar alias
    nr_atendimento DESC,
    dt_registro_order ASC
;