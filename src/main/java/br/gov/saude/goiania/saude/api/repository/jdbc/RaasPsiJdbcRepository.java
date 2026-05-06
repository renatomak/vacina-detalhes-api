package br.gov.saude.goiania.saude.api.repository.jdbc;

import br.gov.saude.goiania.saude.api.dto.RaasPsiArquivoDTO;
import br.gov.saude.goiania.saude.api.repository.RaasPsiRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class RaasPsiJdbcRepository implements RaasPsiRepository {

    private static final Logger logger = LoggerFactory.getLogger(RaasPsiJdbcRepository.class);
    private static final Locale PT_BR = Locale.forLanguageTag("pt-BR");

    /**
     * Subquery que resolve o JOIN empresa x raas_psi:
     *  - Remove zeros à esquerda do CNES (ltrim) para bater com unidade_prestadora_servico
     *  - Exclui farmácias vinculadas (DISTINCT ON garante 1 registro por CNES)
     *  - Ordena pelo menor empresa para garantir a unidade principal
     */
    private static final String EMPRESA_SUBQUERY = """
            LEFT JOIN (
                SELECT DISTINCT ON (cnes_normalizado)
                    empresa,
                    cnes_normalizado,
                    descricao
                FROM (
                    SELECT
                        empresa,
                        ltrim(cnes, '0') AS cnes_normalizado,
                        descricao
                    FROM empresa
                    WHERE cnes IS NOT NULL
                      AND cnes != '0000000'
                      AND descricao NOT ILIKE '%FARMÁCIA%'
                ) e_inner
                ORDER BY cnes_normalizado, empresa ASC
            ) e ON e.cnes_normalizado = rp.unidade_prestadora_servico::varchar
            """;

    private static final String SELECT_CLAUSE = """
            SELECT
                EXTRACT(MONTH FROM rp.competencia)::int AS mes,
                EXTRACT(YEAR FROM rp.competencia)::int  AS ano,
                MIN(rp.dt_cadastro)::date                AS data_geracao,
                rp.unidade_prestadora_servico,
                e.empresa                                AS unidade_id,
                e.descricao                              AS unidade_nome,
                COUNT(*)                                 AS folhas
            FROM raas_psi rp
            """ + EMPRESA_SUBQUERY + """
            WHERE 1=1
            """;

    private static final String GROUP_ORDER_CLAUSE = """
            GROUP BY
                rp.competencia,
                rp.unidade_prestadora_servico,
                e.empresa,
                e.descricao
            ORDER BY
                ano  DESC,
                mes  DESC,
                rp.unidade_prestadora_servico ASC
            """;

    /**
     * COUNT usa o mesmo subquery de empresa para que o filtro por
     * e.descricao (nome da unidade) funcione também na contagem.
     */
    private static final String COUNT_CLAUSE = """
            SELECT COUNT(*) FROM (
                SELECT
                    rp.competencia,
                    rp.unidade_prestadora_servico
                FROM raas_psi rp
            """ + EMPRESA_SUBQUERY + """
                WHERE 1=1
            """;

    private static final String COUNT_GROUP_CLAUSE = """
                GROUP BY rp.competencia, rp.unidade_prestadora_servico
            ) AS sub
            """;

    private final JdbcTemplate jdbcTemplate;

    public RaasPsiJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<RaasPsiArquivoDTO> listarArquivos(LocalDate competencia,
                                                  Long unidadeId,
                                                  String unidade,
                                                  int page,
                                                  int size) {
        StringBuilder sql = new StringBuilder(SELECT_CLAUSE);
        List<Object> params = new ArrayList<>();

        logger.info("[listarArquivos] Filtros recebidos: competencia={}, unidadeId={}, unidade={}, page={}, size={}", competencia, unidadeId, unidade, page, size);

        appendFilters(sql, params, competencia, unidadeId, unidade);

        logger.debug("[listarArquivos] SQL gerado: {}", sql);
        logger.debug("[listarArquivos] Parâmetros: {}", params);

        sql.append(GROUP_ORDER_CLAUSE);
        sql.append(" LIMIT ? OFFSET ?");
        params.add(size);
        params.add((long) page * size);

        try {
            List<RaasPsiArquivoDTO> result = jdbcTemplate.query(sql.toString(), (rs, rowNum) -> {
                int  mesNum      = rs.getInt("mes");
                int  ano         = rs.getInt("ano");
                Date dataGeracao = rs.getDate("data_geracao");

                long idUnidade = rs.getLong("unidade_id");
                if (rs.wasNull()) {
                    idUnidade = rs.getLong("unidade_prestadora_servico");
                }

                String nomeUnidade = rs.getString("unidade_nome");
                if (nomeUnidade == null) {
                    nomeUnidade = "Unidade " + idUnidade;
                }

                String mesNome = Month.of(mesNum).getDisplayName(TextStyle.FULL, PT_BR);
                mesNome = mesNome.substring(0, 1).toUpperCase(PT_BR) + mesNome.substring(1);

                RaasPsiArquivoDTO dto = new RaasPsiArquivoDTO(
                    mesNome,
                    ano,
                    dataGeracao != null ? dataGeracao.toLocalDate() : null,
                    idUnidade,
                    nomeUnidade,
                    "Arquivo Gerado",
                    rs.getLong("folhas")
                );
                logger.trace("[listarArquivos] Linha mapeada: {}", dto);
                return dto;
            }, params.toArray());
            logger.info("[listarArquivos] Total de registros retornados: {}", result.size());
            return result;
        } catch (Exception ex) {
            logger.error("[listarArquivos] Erro ao executar query", ex);
            throw ex;
        }
    }

    @Override
    public long contarArquivos(LocalDate competencia, Long unidadeId, String unidade) {
        StringBuilder sql = new StringBuilder(COUNT_CLAUSE);
        List<Object> params = new ArrayList<>();

        logger.info("[contarArquivos] Filtros recebidos: competencia={}, unidadeId={}, unidade={}", competencia, unidadeId, unidade);

        appendFilters(sql, params, competencia, unidadeId, unidade);

        logger.debug("[contarArquivos] SQL gerado: {}", sql);
        logger.debug("[contarArquivos] Parâmetros: {}", params);

        sql.append(COUNT_GROUP_CLAUSE);

        try {
            Long count = jdbcTemplate.queryForObject(sql.toString(), Long.class, params.toArray());
            logger.info("[contarArquivos] Total encontrado: {}", count);
            return count != null ? count : 0L;
        } catch (Exception ex) {
            logger.error("[contarArquivos] Erro ao executar query", ex);
            throw ex;
        }
    }

    private void appendFilters(StringBuilder sql,
                               List<Object> params,
                               LocalDate competencia,
                               Long unidadeId,
                               String unidade) {
        if (competencia != null) {
            sql.append("  AND rp.competencia = ?\n");
            params.add(Date.valueOf(competencia));
        }
        if (unidadeId != null) {
            sql.append("  AND e.empresa = ?\n");
            params.add(unidadeId);
        }
        if (unidade != null && !unidade.isBlank()) {
            sql.append("  AND e.descricao ILIKE ?\n");
            params.add("%" + unidade.trim() + "%");
        }
        logger.debug("[appendFilters] SQL parcial: {}", sql);
        logger.debug("[appendFilters] Parâmetros parciais: {}", params);
    }
}
