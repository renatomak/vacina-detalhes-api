package br.gov.saude.vacinadetalhesapi.repository;

import br.gov.saude.vacinadetalhesapi.port.ProntuarioRepositoryPort;
import br.gov.saude.vacinadetalhesapi.dto.AtendimentoDTO;
import br.gov.saude.vacinadetalhesapi.mapper.ProntuarioMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.time.LocalDateTime;

@Repository
public class ProntuarioRepositoryAdapter implements ProntuarioRepositoryPort {

    @PersistenceContext
    private EntityManager entityManager;

    private final ProntuarioMapper prontuarioMapper;

    public ProntuarioRepositoryAdapter(ProntuarioMapper prontuarioMapper) {
        this.prontuarioMapper = prontuarioMapper;
    }

    public List<ProntuarioRaw> buscarProntuarioRawPorPaciente(Long pacienteId) {
        String sql = """
            SELECT 
                a.nr_atendimento,                                   -- 0
                a.dt_chegada,                                       -- 1
                e.descricao AS unidade_nome,                        -- 2
                e.telefone AS unidade_telefone,                     -- 3
                ta.ds_tipo_atendimento,                             -- 4
                -- Profissional Principal (Atendimento ou Fallback AIH)
                COALESCE(p.nm_profissional, p_aih.nm_profissional) AS prof_nome,   -- 5
                COALESCE(p.nr_registro, p_aih.nr_registro) AS prof_reg,             -- 6
                COALESCE(oe.sg_orgao_emissor, oe_aih.sg_orgao_emissor) AS prof_cons, -- 7
                tc.cd_cbo AS prof_cbo,                                              -- 8
                tc.ds_cbo AS prof_cbo_ds,                                           -- 9
                ap2.data AS registro_data,                          -- 10
                ap2.tipo_registro AS registro_tipo_id,              -- 11
                ap2.descricao AS registro_conteudo,                 -- 12
                cr.descricao AS classificacao_risco_nome,           -- 13
                a2.dt_cadastro AS aih_dt,                           -- 14
                a2.principais_sinais AS aih_sinais,                 -- 15
                a2.condicoes_just_intern AS aih_cond,               -- 16
                a2.principais_resultados AS aih_res,                -- 17
                a2.diagnostico_inicial AS aih_diag,                 -- 18
                CASE WHEN a2.nr_atendimento IS NOT NULL 
                     THEN 'Sim' ELSE 'Não' END AS possui_aih       -- 19
            FROM atendimento a
            INNER JOIN empresa e ON e.empresa = a.empresa
            LEFT JOIN atendimento_prontuario ap2 ON ap2.nr_atendimento = a.nr_atendimento
            LEFT JOIN profissional p ON p.cd_profissional = a.cd_profissional
            LEFT JOIN orgao_emissor oe ON oe.cd_orgao_emissor = p.cd_con_classe
            LEFT JOIN tabela_cbo tc ON tc.cd_cbo = a.cd_cbo
            LEFT JOIN classificacao_risco cr ON cr.cd_classificacao_risco = a.classificacao_risco
            LEFT JOIN natureza_procura_tp_atendimento npta ON npta.cd_nat_proc_tp_atendimento = a.cd_nat_proc_tp_atendimento
            LEFT JOIN tipo_atendimento ta ON ta.cd_tp_atendimento = npta.cd_tp_atendimento
            LEFT JOIN aih a2 ON a2.nr_atendimento = a.nr_atendimento
            LEFT JOIN profissional p_aih ON p_aih.cd_profissional = a2.cd_profissional
            LEFT JOIN orgao_emissor oe_aih ON oe_aih.cd_orgao_emissor = p_aih.cd_con_classe
            WHERE a.cd_usu_cadsus = :idPaciente 
              AND a.status IN (1, 4, 5, 8, 9)
              AND NOT (a2.nr_atendimento IS NULL AND a.dt_atendimento IS NULL)
            ORDER BY a.dt_chegada DESC, ap2.data ASC
            """;

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("idPaciente", pacienteId);

        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();

        return results.stream()
            .map(row -> (ProntuarioRaw) new ProntuarioRawRecord(
                row[0] != null ? ((Number) row[0]).longValue() : null,
                convertToLocalDateTime(row[1]),
                (String) row[2],
                (String) row[3],
                (String) row[4],
                (String) row[5],
                (String) row[6],
                (String) row[7],
                (String) row[8],
                (String) row[9],
                convertToLocalDateTime(row[10]),
                row[11] != null ? ((Number) row[11]).intValue() : null,
                (String) row[12],
                (String) row[13],
                convertToLocalDateTime(row[14]), // aih_dt
                (String) row[15],                // aih_sinais
                (String) row[16],                // aih_cond
                (String) row[17],                // aih_res
                (String) row[18],                // aih_diag
                "Sim".equals(row[19])            // possui_aih
            ))
            .toList();
    }

    @Override
    public List<AtendimentoDTO> buscarAtendimentosComRegistrosPorPaciente(Long pacienteId) {
        var raws = buscarProntuarioRawPorPaciente(pacienteId);
        return prontuarioMapper.toProntuarioDTO(pacienteId, raws).atendimentos();
    }

    private LocalDateTime convertToLocalDateTime(Object obj) {
        if (obj == null) return null;
        if (obj instanceof java.sql.Timestamp ts) return ts.toLocalDateTime();
        if (obj instanceof java.time.LocalDateTime ldt) return ldt;
        return null;
    }
}
