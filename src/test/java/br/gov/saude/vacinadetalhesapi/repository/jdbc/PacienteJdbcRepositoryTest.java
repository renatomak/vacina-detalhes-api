package br.gov.saude.vacinadetalhesapi.repository.jdbc;

import br.gov.saude.vacinadetalhesapi.dto.PacienteDTO;
import br.gov.saude.vacinadetalhesapi.dto.PacienteResumoDTO;
import br.gov.saude.vacinadetalhesapi.mapper.PacienteRowMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

class PacienteJdbcRepositoryTest {

    @SuppressWarnings("unchecked")
    @Test
    void deveBuscarDetalhePorCpf() {
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        PacienteRowMapper rowMapper = Mockito.mock(PacienteRowMapper.class);
        RowMapper<PacienteDTO> detalheMapper = Mockito.mock(RowMapper.class);
        Mockito.when(rowMapper.detalhe()).thenReturn(detalheMapper);

        PacienteDTO paciente = Mockito.mock(PacienteDTO.class);
        Mockito.when(jdbcTemplate.query(Mockito.anyString(), Mockito.eq(detalheMapper), Mockito.eq("123")))
                .thenReturn(List.of(paciente));

        PacienteJdbcRepository repository = new PacienteJdbcRepository(jdbcTemplate, rowMapper);

        var resultado = repository.buscarDetalhePorCpf("123");

        Assertions.assertTrue(resultado.isPresent());
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(jdbcTemplate).query(sqlCaptor.capture(), Mockito.eq(detalheMapper), Mockito.eq("123"));
        Assertions.assertTrue(sqlCaptor.getValue().contains("p.cpf = ?"));
    }

    @SuppressWarnings("unchecked")
    @Test
    void deveBuscarDetalhePorIdVazio() {
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        PacienteRowMapper rowMapper = Mockito.mock(PacienteRowMapper.class);
        RowMapper<PacienteDTO> detalheMapper = Mockito.mock(RowMapper.class);
        Mockito.when(rowMapper.detalhe()).thenReturn(detalheMapper);

        Mockito.when(jdbcTemplate.query(Mockito.anyString(), Mockito.eq(detalheMapper), Mockito.eq(1L)))
                .thenReturn(List.of());

        PacienteJdbcRepository repository = new PacienteJdbcRepository(jdbcTemplate, rowMapper);

        var resultado = repository.buscarDetalhePorId(1L);

        Assertions.assertTrue(resultado.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    void deveBuscarResumoPorNome() {
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        PacienteRowMapper rowMapper = Mockito.mock(PacienteRowMapper.class);
        RowMapper<PacienteResumoDTO> resumoMapper = Mockito.mock(RowMapper.class);
        Mockito.when(rowMapper.resumo()).thenReturn(resumoMapper);

        PacienteResumoDTO resumo = Mockito.mock(PacienteResumoDTO.class);
        Mockito.when(jdbcTemplate.query(Mockito.anyString(), Mockito.eq(resumoMapper), Mockito.eq("maria"), Mockito.eq(30)))
                .thenReturn(List.of(resumo));

        PacienteJdbcRepository repository = new PacienteJdbcRepository(jdbcTemplate, rowMapper);

        var resultado = repository.buscarResumoPorNome("maria", 30);

        Assertions.assertEquals(1, resultado.size());
    }
}


