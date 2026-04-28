package br.gov.saude.vacinadetalhesapi.repository;

import br.gov.saude.vacinadetalhesapi.port.ProntuarioRepositoryPort;
import br.gov.saude.vacinadetalhesapi.dto.AtendimentoDTO;
import br.gov.saude.vacinadetalhesapi.mapper.ProntuarioMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProntuarioRepositoryAdapter implements ProntuarioRepositoryPort {

    @PersistenceContext
    private EntityManager entityManager;

    private final ProntuarioMapper prontuarioMapper;

    public ProntuarioRepositoryAdapter(ProntuarioMapper prontuarioMapper) {
        this.prontuarioMapper = prontuarioMapper;
    }

    void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public List<ProntuarioRaw> buscarProntuarioRawPorPaciente(Long pacienteId) {
        String sql = "SELECT " +
                "a.nr_atendimento, a.dt_chegada, " +
                "e.descricao AS unidade_nome, e.telefone AS unidade_telefone, " +
                "ta.ds_tipo_atendimento, " +
                "p.nm_profissional, p.nr_registro AS profissional_registro, " +
                "oe.sg_orgao_emissor AS profissional_tipo_conselho, " +
                "tc.cd_cbo AS profissional_cbo, tc.ds_cbo AS profissional_cbo_descricao, " +
                "ap2.data AS registro_data, ap2.tipo_registro AS registro_tipo_id, " +
                "ap2.descricao AS registro_conteudo, " +
                "cr.descricao AS classificacao_risco_nome " +
                "FROM atendimento a " +
                "INNER JOIN empresa e ON e.empresa = a.empresa " +
                "INNER JOIN atendimento_prontuario ap2 ON ap2.nr_atendimento = a.nr_atendimento " +
                "LEFT JOIN profissional p ON p.cd_profissional = ap2.cd_profissional " +
                "LEFT JOIN orgao_emissor oe ON oe.cd_orgao_emissor = p.cd_con_classe " +
                "LEFT JOIN tabela_cbo tc ON tc.cd_cbo = ap2.cd_cbo " +
                "LEFT JOIN classificacao_risco cr ON cr.cd_classificacao_risco = a.classificacao_risco " +
                "LEFT JOIN natureza_procura_tp_atendimento npta ON npta.cd_nat_proc_tp_atendimento = a.cd_nat_proc_tp_atendimento " +
                "LEFT JOIN tipo_atendimento ta ON ta.cd_tp_atendimento = npta.cd_tp_atendimento " +
                "WHERE a.cd_usu_cadsus = :idPaciente AND a.status IN (1, 4, 5, 8, 9) " +
                "ORDER BY a.dt_chegada DESC, ap2.data ASC";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("idPaciente", pacienteId);
        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
        return results.stream()
            .map(row -> (ProntuarioRaw) new ProntuarioRawRecord(
                row[0] != null ? ((Number) row[0]).longValue() : null,
                row[1] != null ? (row[1] instanceof java.sql.Timestamp ? ((java.sql.Timestamp) row[1]).toLocalDateTime() : (java.time.LocalDateTime) row[1]) : null,
                (String) row[2],
                (String) row[3],
                (String) row[4],
                (String) row[5],
                (String) row[6],
                (String) row[7],
                (String) row[8],
                (String) row[9],
                row[10] != null ? (row[10] instanceof java.sql.Timestamp ? ((java.sql.Timestamp) row[10]).toLocalDateTime() : (java.time.LocalDateTime) row[10]) : null,
                row[11] != null ? ((Number) row[11]).intValue() : null,
                (String) row[12],
                (String) row[13]
            ))
            .toList();
    }

    @Override
    public List<AtendimentoDTO> buscarAtendimentosComRegistrosPorPaciente(Long pacienteId) {
        var raws = buscarProntuarioRawPorPaciente(pacienteId);
        return prontuarioMapper.toProntuarioDTO(pacienteId, raws).atendimentos();
    }
}
