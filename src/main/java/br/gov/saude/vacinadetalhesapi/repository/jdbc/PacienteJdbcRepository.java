package br.gov.saude.vacinadetalhesapi.repository.jdbc;

import br.gov.saude.vacinadetalhesapi.dto.PacienteDTO;
import br.gov.saude.vacinadetalhesapi.dto.PacienteResumoDTO;
import br.gov.saude.vacinadetalhesapi.mapper.PacienteRowMapper;
import br.gov.saude.vacinadetalhesapi.repository.PacienteRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PacienteJdbcRepository implements PacienteRepository {

    private static final String PACIENTE_DETALHE_SQL = """
            SELECT
                p.cd_usu_cadsus AS id,
                p.nm_usuario AS nome,
                p.cpf AS cpf,
                p.sg_sexo AS sexo,
                p.nm_mae AS nome_mae,
                p.nm_pai AS nome_pai,
                p.dt_nascimento AS data_nascimento,
                p.nr_telefone AS telefone,
                e.keyword AS keyword,
                tl.ds_tipo_logradouro AS tipo_logradouro,
                e.nm_logradouro AS logradouro,
                e.nm_comp_logradouro AS complemento,
                e.nr_logradouro AS numero,
                e.cep AS cep,
                e.nm_bairro AS bairro,
                c.cod_cid AS cidade_id,
                c.descricao AS cidade,
                uf.sigla AS uf
            FROM usuario_cadsus p
            INNER JOIN endereco_usuario_cadsus e ON p.cd_endereco = e.cd_endereco
            INNER JOIN cidade c ON e.cod_cid = c.cod_cid
            INNER JOIN estado uf ON c.cod_est = uf.cod_est
            INNER JOIN tipo_logradouro_cadsus tl ON e.cd_tipo_logradouro = tl.cd_tipo_logradouro
            WHERE %s
            """;

    private static final String PACIENTE_RESUMO_POR_NOME_SQL = """
            SELECT
                cd_usu_cadsus AS id,
                nm_usuario AS nome,
                cpf,
                dt_nascimento AS data_nascimento
            FROM usuario_cadsus
            WHERE nm_usuario ILIKE '%' || ? || '%'
            ORDER BY nm_usuario
            LIMIT ?
            """;

    private final JdbcTemplate jdbcTemplate;
    private final PacienteRowMapper rowMapper;

    public PacienteJdbcRepository(JdbcTemplate jdbcTemplate, PacienteRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = rowMapper;
    }

    @Override
    public Optional<PacienteDTO> buscarDetalhePorCpf(String cpfSemMascara) {
        List<PacienteDTO> resultados = jdbcTemplate.query(
                PACIENTE_DETALHE_SQL.formatted("p.cpf = ?"),
                rowMapper.detalhe(),
                cpfSemMascara
        );
        return resultados.stream().findFirst();
    }

    @Override
    public Optional<PacienteDTO> buscarDetalhePorId(Long id) {
        List<PacienteDTO> resultados = jdbcTemplate.query(
                PACIENTE_DETALHE_SQL.formatted("p.cd_usu_cadsus = ?"),
                rowMapper.detalhe(),
                id
        );
        return resultados.stream().findFirst();
    }

    @Override
    public List<PacienteResumoDTO> buscarResumoPorNome(String nomeParcial, int limite) {
        return jdbcTemplate.query(PACIENTE_RESUMO_POR_NOME_SQL, rowMapper.resumo(), nomeParcial, limite);
    }
}


