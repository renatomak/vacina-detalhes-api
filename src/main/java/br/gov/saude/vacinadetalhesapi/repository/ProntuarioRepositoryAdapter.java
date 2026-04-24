package br.gov.saude.vacinadetalhesapi.repository;

import br.gov.saude.vacinadetalhesapi.domain.ProntuarioItem;
import br.gov.saude.vacinadetalhesapi.port.ProntuarioRepositoryPort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class ProntuarioRepositoryAdapter implements ProntuarioRepositoryPort {

    @PersistenceContext
    private EntityManager entityManager;

    // Setter para uso em testes
    void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<ProntuarioItem> buscarHistoricoPorPaciente(Long pacienteId) {
        String sql = "SELECT " +
                "TO_CHAR(ep.dt_historico, 'DD/MM/YYYY HH24:MI:SS') AS dt_registro, " +
                "CONCAT(p.nm_profissional, ' - ', OE.SG_ORGAO_EMISSOR, ': ', P.NR_REGISTRO, ' CBO: (', TC.cd_cbo, ') ', TC.ds_cbo) AS profissional, " +
                "e.descricao AS unidade, " +
                "CASE WHEN ap.dt_avaliacao IS NULL THEN 'Evolução' ELSE 'Avaliação' END AS tipo_registro, " +
                "cr.descricao AS classificacao_risco, " +
                "REGEXP_REPLACE(COALESCE(ep.ds_prontuario, '') || ' ' || COALESCE(ap.historico, ''), '<[^>]*>', '', 'g') AS conteudo " +
                "FROM atendimento a " +
                "INNER JOIN empresa e ON e.empresa = a.empresa " +
                "INNER JOIN evolucao_prontuario ep ON ep.nr_atendimento = a.nr_atendimento " +
                "LEFT JOIN atendimento_primario ap ON ap.nr_atendimento = a.nr_atendimento " +
                "LEFT JOIN profissional p ON p.cd_profissional = a.cd_profissional " +
                "LEFT JOIN tabela_cbo tc ON tc.cd_cbo = a.cd_cbo " +
                "LEFT JOIN ORGAO_EMISSOR OE ON OE.CD_ORGAO_EMISSOR = P.cd_CON_CLASSE " +
                "LEFT JOIN classificacao_risco cr ON cr.cd_classificacao_risco = a.classificacao_risco " +
                "WHERE a.cd_usu_cadsus = :pacienteId AND a.status <> 2 " +
                "ORDER BY ep.dt_historico DESC";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("pacienteId", pacienteId);
        List<Object[]> results = query.getResultList();
        return results.stream().map(row -> new ProntuarioItem(
                (String) row[0],
                (String) row[1],
                (String) row[2],
                (String) row[3],
                (String) row[4],
                (String) row[5]
        )).toList();
    }
}
