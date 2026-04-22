package br.gov.saude.vacinadetalhesapi.repository.jdbc;

import br.gov.saude.vacinadetalhesapi.dto.VacinaDetalheDTO;
import br.gov.saude.vacinadetalhesapi.dto.VacinaResumoDTO;
import br.gov.saude.vacinadetalhesapi.mapper.VacinaRowMapper;
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
        VacinaRowMapper rowMapper = Mockito.mock(VacinaRowMapper.class);
        RowMapper<VacinaResumoDTO> resumoMapper = Mockito.mock(RowMapper.class);
        Mockito.when(rowMapper.resumo()).thenReturn(resumoMapper);

        VacinaResumoDTO dto = Mockito.mock(VacinaResumoDTO.class);
        Mockito.when(jdbcTemplate.query(Mockito.anyString(), Mockito.eq(resumoMapper), Mockito.eq(1L)))
                .thenReturn(List.of(dto));

        VacinaJdbcRepository repository = new VacinaJdbcRepository(jdbcTemplate, rowMapper);

        List<VacinaResumoDTO> retorno = repository.listarPorPacienteId(1L);

        Assertions.assertEquals(1, retorno.size());
    }

    @SuppressWarnings("unchecked")
    @Test
    void deveBuscarDetalhePorAplicacaoIdComResultado() {
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        VacinaRowMapper rowMapper = Mockito.mock(VacinaRowMapper.class);
        RowMapper<VacinaDetalheDTO> detalheMapper = Mockito.mock(RowMapper.class);
        Mockito.when(rowMapper.detalhe()).thenReturn(detalheMapper);

        VacinaDetalheDTO detalhe = Mockito.mock(VacinaDetalheDTO.class);
        Mockito.when(jdbcTemplate.query(Mockito.anyString(), Mockito.eq(detalheMapper), Mockito.eq(99L)))
                .thenReturn(List.of(detalhe));

        VacinaJdbcRepository repository = new VacinaJdbcRepository(jdbcTemplate, rowMapper);

        var retorno = repository.buscarDetalhePorAplicacaoId(99L);

        Assertions.assertTrue(retorno.isPresent());
    }

    @SuppressWarnings("unchecked")
    @Test
    void deveBuscarDetalhePorAplicacaoIdSemResultado() {
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        VacinaRowMapper rowMapper = Mockito.mock(VacinaRowMapper.class);
        RowMapper<VacinaDetalheDTO> detalheMapper = Mockito.mock(RowMapper.class);
        Mockito.when(rowMapper.detalhe()).thenReturn(detalheMapper);

        Mockito.when(jdbcTemplate.query(Mockito.anyString(), Mockito.eq(detalheMapper), Mockito.eq(99L)))
                .thenReturn(List.of());

        VacinaJdbcRepository repository = new VacinaJdbcRepository(jdbcTemplate, rowMapper);

        var retorno = repository.buscarDetalhePorAplicacaoId(99L);

        Assertions.assertTrue(retorno.isEmpty());
    }
}


