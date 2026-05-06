package br.gov.saude.goiania.saude.api.repository.jdbc;

import br.gov.saude.goiania.saude.api.dto.VacinaDetalheDTO;
import br.gov.saude.goiania.saude.api.dto.VacinaResumoDTO;
import br.gov.saude.goiania.saude.api.mapper.VacinaRawRowMapper;
import br.gov.saude.goiania.saude.api.mapper.VacinaRaw;
import br.gov.saude.goiania.saude.api.mapper.VacinaMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

class VacinaJdbcRepositoryTest {

    @SuppressWarnings("unchecked")
    @Test
    void deveListarPorPacienteId() {
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        VacinaRawRowMapper rawRowMapper = Mockito.mock(VacinaRawRowMapper.class);
        RowMapper<VacinaRaw> resumoMapper = Mockito.mock(RowMapper.class);
        VacinaMapper vacinaMapper = Mockito.mock(VacinaMapper.class);
        Mockito.when(rawRowMapper.resumo()).thenReturn(resumoMapper);

        VacinaRaw raw = Mockito.mock(VacinaRaw.class);
        VacinaResumoDTO dto = Mockito.mock(VacinaResumoDTO.class);
        Mockito.when(jdbcTemplate.query(Mockito.anyString(), Mockito.eq(resumoMapper), Mockito.eq(1L)))
                .thenReturn(List.of(raw));
        Mockito.when(vacinaMapper.toVacinaResumoDTO(raw)).thenReturn(dto);

        VacinaJdbcRepository repository = new VacinaJdbcRepository(jdbcTemplate, rawRowMapper, vacinaMapper);

        List<VacinaResumoDTO> retorno = repository.listarPorPacienteId(1L);

        Assertions.assertEquals(1, retorno.size());
    }

    @SuppressWarnings("unchecked")
    @Test
    void deveBuscarDetalhePorAplicacaoIdComResultado() {
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        VacinaRawRowMapper rawRowMapper = Mockito.mock(VacinaRawRowMapper.class);
        RowMapper<VacinaRaw> detalheMapper = Mockito.mock(RowMapper.class);
        VacinaMapper vacinaMapper = Mockito.mock(VacinaMapper.class);
        Mockito.when(rawRowMapper.detalhe()).thenReturn(detalheMapper);

        VacinaRaw raw = Mockito.mock(VacinaRaw.class);
        VacinaDetalheDTO detalhe = Mockito.mock(VacinaDetalheDTO.class);
        Mockito.when(jdbcTemplate.query(Mockito.anyString(), Mockito.eq(detalheMapper), Mockito.eq(99L)))
                .thenReturn(List.of(raw));
        Mockito.when(vacinaMapper.toVacinaDetalheDTO(raw)).thenReturn(detalhe);

        VacinaJdbcRepository repository = new VacinaJdbcRepository(jdbcTemplate, rawRowMapper, vacinaMapper);

        var retorno = repository.buscarDetalhePorAplicacaoId(99L);

        Assertions.assertTrue(retorno.isPresent());
    }

    @SuppressWarnings("unchecked")
    @Test
    void deveBuscarDetalhePorAplicacaoIdSemResultado() {
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        VacinaRawRowMapper rawRowMapper = Mockito.mock(VacinaRawRowMapper.class);
        RowMapper<VacinaRaw> detalheMapper = Mockito.mock(RowMapper.class);
        VacinaMapper vacinaMapper = Mockito.mock(VacinaMapper.class);
        Mockito.when(rawRowMapper.detalhe()).thenReturn(detalheMapper);

        Mockito.when(jdbcTemplate.query(Mockito.anyString(), Mockito.eq(detalheMapper), Mockito.eq(99L)))
                .thenReturn(List.of());

        VacinaJdbcRepository repository = new VacinaJdbcRepository(jdbcTemplate, rawRowMapper, vacinaMapper);

        var retorno = repository.buscarDetalhePorAplicacaoId(99L);

        Assertions.assertTrue(retorno.isEmpty());
    }
}
