package br.gov.saude.goiania.saude.api.repository;

import org.springframework.stereotype.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import java.util.List;

@Repository
public class ExameRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public List<Object[]> buscarExamesPorAtendimento(Long nrAtendimento) {
        String sql = "SELECT te.ds_tp_exame AS grupo, ARRAY_AGG(ep.ds_procedimento ORDER BY ep.ds_procedimento) AS itens " +
                "FROM public.atendimento_exame ae " +
                "INNER JOIN public.tipo_exame te ON ae.cd_tp_exame = te.cd_tp_exame " +
                "INNER JOIN public.atendimento_exame_item aei ON ae.cd_atendimento_exame = aei.cd_atendimento_exame " +
                "INNER JOIN public.exame_procedimento ep ON aei.cd_exame_procedimento = ep.cd_exame_procedimento " +
                "WHERE ae.nr_atendimento = :nrAtendimento " +
                "GROUP BY te.ds_tp_exame";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("nrAtendimento", nrAtendimento);
        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();
        return results;
    }
}
