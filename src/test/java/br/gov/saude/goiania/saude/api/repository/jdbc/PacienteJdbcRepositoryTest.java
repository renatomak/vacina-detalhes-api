package br.gov.saude.goiania.saude.api.repository.jdbc;

import br.gov.saude.goiania.saude.api.dto.PacienteDTO;
import br.gov.saude.goiania.saude.api.dto.PacienteResumoDTO;
import br.gov.saude.goiania.saude.api.mapper.PacienteMapper;
import br.gov.saude.goiania.saude.api.mapper.PacienteRaw;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

class PacienteJdbcRepositoryTest {

    @SuppressWarnings("unchecked")
    @Test
    void deveBuscarDetalhePorCpf() {
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        PacienteMapper mapper = Mockito.mock(PacienteMapper.class);
        PacienteRaw raw = new PacienteRaw();
        PacienteDTO paciente = Mockito.mock(PacienteDTO.class);
        Mockito.when(jdbcTemplate.query(Mockito.anyString(), Mockito.<org.springframework.jdbc.core.RowMapper<PacienteRaw>>any(), Mockito.eq("123")))
                .thenReturn(List.of(raw));
        Mockito.when(mapper.toPacienteDTO(raw)).thenReturn(paciente);

        PacienteJdbcRepository repository = new PacienteJdbcRepository(jdbcTemplate, mapper);

        var resultado = repository.buscarDetalhePorCpf("123");

        Assertions.assertTrue(resultado.isPresent());
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(jdbcTemplate).query(sqlCaptor.capture(), Mockito.<org.springframework.jdbc.core.RowMapper<PacienteRaw>>any(), Mockito.eq("123"));
        Assertions.assertTrue(sqlCaptor.getValue().contains("p.cpf = ?"));
    }

    @SuppressWarnings("unchecked")
    @Test
    void deveBuscarDetalhePorIdVazio() {
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        PacienteMapper mapper = Mockito.mock(PacienteMapper.class);
        Mockito.when(jdbcTemplate.query(Mockito.anyString(), Mockito.<org.springframework.jdbc.core.RowMapper<PacienteRaw>>any(), Mockito.eq(1L)))
                .thenReturn(List.of());

        PacienteJdbcRepository repository = new PacienteJdbcRepository(jdbcTemplate, mapper);

        var resultado = repository.buscarDetalhePorId(1L);

        Assertions.assertTrue(resultado.isEmpty());
    }

    @SuppressWarnings("unchecked")
    @Test
    void deveBuscarResumoPorNome() {
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        PacienteMapper mapper = Mockito.mock(PacienteMapper.class);
        PacienteResumoDTO resumo = Mockito.mock(PacienteResumoDTO.class);
        Mockito.when(jdbcTemplate.query(Mockito.anyString(), Mockito.<org.springframework.jdbc.core.RowMapper<PacienteResumoDTO>>any(), Mockito.eq("%maria%"), Mockito.eq(30)))
                .thenReturn(List.of(resumo));

        PacienteJdbcRepository repository = new PacienteJdbcRepository(jdbcTemplate, mapper);

        var resultado = repository.buscarResumoPorNome("maria", 30);

        Assertions.assertEquals(1, resultado.size());
    }
}


