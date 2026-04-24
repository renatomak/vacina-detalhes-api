package br.gov.saude.vacinadetalhesapi.repository;

import br.gov.saude.vacinadetalhesapi.domain.ProntuarioItem;
import br.gov.saude.vacinadetalhesapi.port.ProntuarioRepositoryPort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Repository;
import org.jsoup.Jsoup;

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
            "ORDER BY ep.dt_historico\\:\\:date DESC, ep.dt_historico\\:\\:time ASC";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("pacienteId", pacienteId);

        @SuppressWarnings("unchecked")
        List<Object[]> results = query.getResultList();

        return results.stream().map(row -> new ProntuarioItem(
            (String) row[2], // DT_REGISTRO
            (String) row[0], // profissional
            (String) row[5], // unidade
            (String) row[4], // tipo
            (String) row[7], // classificacao_risco
            limparHtml((String) row[6]) // HISTORICO_EVOLUCAO (conteudo limpo)
        )).toList();
    }

    private String limparHtml(String texto) {
        if (texto == null || texto.isBlank()) return "";

        // 1. Preservar quebras de linha básicas
        String preparoTexto = texto
            .replaceAll("(?i)<br\\s*/?>", " ___BR___ ")
            .replaceAll("(?i)</p>", " ___BR___ ")
            .replaceAll("(?i)</div>", " ___BR___ ");

        // 2. Jsoup para extrair o texto limpo de tags
        String textoLimpo = org.jsoup.Jsoup.parseBodyFragment(preparoTexto).text();

        // 3. RECUPERAR QUEBRAS E LIMPEZA PESADA (O PONTO CHAVE)
        textoLimpo = textoLimpo.replace("___BR___", "\n");

        /**
         * REGEX EXPLICADA:
         * [^\x00-\x7F\xA0-\xFF\n]
         * ^ : Negação (manter o que estiver aqui)
         * \x00-\x7F : Tabela ASCII padrão (letras, números, pontuação)
         * \xA0-\xFF : Tabela Latin-1 (Acentos: á, é, í, ó, ú, ç, ã, etc)
         * \n : Quebras de linha
         * Tudo que não for isso (emojis, Variation Selectors \uFE0F, símbolos invisíveis) será removido.
         */
        textoLimpo = textoLimpo.replaceAll("[^\\x00-\\x7F\\xA0-\\xFF\\n]", "");

        // 4. Normalização de espaços residuais
        return textoLimpo
            .replaceAll("(?m)^[ \t]+", "")    // Remove espaços no início das linhas
            .replaceAll("(?m)[ \t]+$", "")    // Remove espaços no fim das linhas
            .replaceAll("[ ]{2,}", " ")       // Transforma espaços duplos em simples
            .trim();
    }
}
