package br.gov.saude.vacinadetalhesapi.repository.jdbc;

import br.gov.saude.vacinadetalhesapi.dto.PacienteDTO;
import br.gov.saude.vacinadetalhesapi.dto.PacienteResumoDTO;
import br.gov.saude.vacinadetalhesapi.mapper.PacienteMapper;
import br.gov.saude.vacinadetalhesapi.mapper.PacienteRaw;
import br.gov.saude.vacinadetalhesapi.repository.PacienteRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class PacienteJdbcRepository implements PacienteRepository {

    private static final String PACIENTE_DETALHE_SQL = """
            SELECT
                p.cd_usu_cadsus AS id, p.nm_usuario AS nome, p.cpf, p.sg_sexo AS sexo,
                p.nm_mae AS nome_mae, p.nm_pai AS nome_pai, p.dt_nascimento AS data_nascimento,
                p.nr_telefone AS telefone, e.keyword, tl.ds_tipo_logradouro AS tipo_logradouro,
                e.nm_logradouro AS logradouro, e.nm_comp_logradouro AS complemento,
                e.nr_logradouro AS numero, e.cep, e.nm_bairro AS bairro,
                c.cod_cid AS cidade_id, c.descricao AS cidade, uf.sigla AS uf
            FROM usuario_cadsus p
            INNER JOIN endereco_usuario_cadsus e ON p.cd_endereco = e.cd_endereco
            INNER JOIN cidade c ON e.cod_cid = c.cod_cid
            INNER JOIN estado uf ON c.cod_est = uf.cod_est
            INNER JOIN tipo_logradouro_cadsus tl ON e.cd_tipo_logradouro = tl.cd_tipo_logradouro
            WHERE %s
            """;

    private static final String PACIENTE_RESUMO_POR_NOME_SQL = """
            SELECT cd_usu_cadsus AS id, nm_usuario AS nome, cpf, dt_nascimento AS data_nascimento
            FROM usuario_cadsus
            WHERE nm_usuario ILIKE ?
            ORDER BY nm_usuario
            LIMIT ?
            """;

    private final JdbcTemplate jdbcTemplate;
    private final PacienteMapper pacienteMapper;

    public PacienteJdbcRepository(JdbcTemplate jdbcTemplate, PacienteMapper pacienteMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.pacienteMapper = pacienteMapper;
    }

    @Override
    public Optional<PacienteDTO> buscarDetalhePorCpf(String cpf) {
        return buscarUm(PACIENTE_DETALHE_SQL.formatted("p.cpf = ?"), cpf);
    }

    @Override
    public Optional<PacienteDTO> buscarDetalhePorId(Long id) {
        return buscarUm(PACIENTE_DETALHE_SQL.formatted("p.cd_usu_cadsus = ?"), id);
    }

    @Override
    public List<PacienteResumoDTO> buscarResumoPorNome(String nomeParcial, int limite) {
        String queryParam = "%" + nomeParcial + "%";
        return jdbcTemplate.query(PACIENTE_RESUMO_POR_NOME_SQL, (rs, rowNum) -> {
            Long id = rs.getLong("id");
            String nome = rs.getString("nome");
            String cpf = rs.getString("cpf");
            java.sql.Date data = rs.getDate("data_nascimento");
            java.time.LocalDate dataNascimento = (data != null) ? data.toLocalDate() : null;
            return new PacienteResumoDTO(id, nome, cpf, dataNascimento);
        }, queryParam, limite);
    }

    private Optional<PacienteDTO> buscarUm(String sql, Object... params) {
        List<PacienteRaw> results = jdbcTemplate.query(sql, this::mapRowToPacienteRaw, params);
        return results.stream()
            .findFirst()
            .map(pacienteMapper::toPacienteDTO);
    }

    private PacienteRaw mapRowToPacienteRaw(ResultSet rs, int rowNum) throws SQLException {
        PacienteRaw raw = new PacienteRaw();
        raw.id = rs.getLong("id");
        raw.nome = rs.getString("nome");
        raw.cpf = rs.getString("cpf");
        raw.sexo = rs.getString("sexo");
        raw.nomeMae = rs.getString("nome_mae");
        raw.nomePai = rs.getString("nome_pai");

        java.sql.Date data = rs.getObject("data_nascimento", java.sql.Date.class);
        raw.dataNascimento = (data != null) ? data.toLocalDate() : null;

        raw.telefone = rs.getString("telefone");
        raw.keyword = rs.getString("keyword");
        raw.tipoLogradouro = rs.getString("tipo_logradouro");
        raw.logradouro = rs.getString("logradouro");
        raw.complemento = rs.getString("complemento");
        raw.numero = rs.getString("numero");
        raw.cep = rs.getString("cep");
        raw.bairro = rs.getString("bairro");
        raw.cidadeId = rs.getLong("cidade_id");
        raw.cidade = rs.getString("cidade");
        raw.uf = rs.getString("uf");
        return raw;
    }
}
