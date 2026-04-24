    @Override
    public List<ProntuarioItem> buscarHistoricoPorPaciente(Long pacienteId) {
        String sql = "SELECT " +
                "CONCAT(p.nm_profissional,' - ',OE.SG_ORGAO_EMISSOR,': ',P.NR_REGISTRO,'  ',' CBO: (',TC.cd_cbo, ') ',TC.ds_cbo) AS profissional, " +
                "ta.ds_tipo_atendimento as TIPO_ATEND, " +
                "TO_CHAR(ep.dt_historico, 'DD/MM/YYYY HH24:MI:SS') AS DT_REGISTRO, " +
                "a.nr_atendimento, " +
                "CASE WHEN ap.dt_avaliacao IS NULL THEN 'Evolução' ELSE 'Avaliação' END AS tipo, " +
                "e.descricao AS unidade, " +
                "concat(ep.ds_prontuario,' ',ap.historico) as HISTORICO_EVOLUCAO, " +
                "cr.descricao as classificacao_risco " +
                "FROM atendimento a " +
                "INNER JOIN usuario_cadsus uc ON uc.cd_usu_cadsus = a.cd_usu_cadsus " +
                "INNER JOIN empresa e ON e.empresa = a.empresa " +
                "LEFT JOIN atendimento_primario ap ON ap.nr_atendimento = a.nr_atendimento " +
                "INNER join evolucao_prontuario ep on ep.nr_atendimento = a.nr_atendimento " +
                "LEFT JOIN profissional p ON p.cd_profissional = a.cd_profissional " +
                "LEFT JOIN tabela_cbo tc ON tc.cd_cbo = a.cd_cbo " +
                "LEFT JOIN ORGAO_EMISSOR OE ON OE.CD_ORGAO_EMISSOR = P.cd_CON_CLASSE " +
                "LEFT JOIN classificacao_atendimento ca ON ca.cd_cla_atendimento = a.cd_cla_atendimento " +
                "LEFT JOIN classificacao_risco cr ON cr.cd_classificacao_risco = a.classificacao_risco " +
                "left JOIN natureza_procura_tp_atendimento npta on npta.cd_nat_proc_tp_atendimento = a.cd_nat_proc_tp_atendimento  " +
                "LEFT JOIN tipo_atendimento ta ON ta.cd_TP_atendimento = NPTA.cd_TP_atendimento " +
                "WHERE uc.cd_usu_cadsus = :pacienteId AND a.status <> 2 " +
                "order by ep.dt_historico::date desc, ep.dt_historico::time asc";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("pacienteId", pacienteId);
        List<Object[]> results = query.getResultList();
        return results.stream().map(row -> new ProntuarioItem(
                (String) row[2], // DT_REGISTRO
                (String) row[0], // profissional
                (String) row[6], // unidade
                (String) row[4], // tipo
                (String) row[8], // classificacao_risco
                (String) row[7]  // HISTORICO_EVOLUCAO
        )).toList();
    }
