package br.gov.saude.vacinadetalhesapi.mapper;

import br.gov.saude.vacinadetalhesapi.dto.PacienteDTO;
import br.gov.saude.vacinadetalhesapi.dto.PacienteResumoDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.time.LocalDate;

class PacienteRowMapperTest {

    private final PacienteRowMapper mapper = new PacienteRowMapper();

    @Test
    void deveMapearDetalheComLocalDate() throws Exception {
        ResultSet rs = Mockito.mock(ResultSet.class);
        Mockito.when(rs.getLong("id")).thenReturn(1L);
        Mockito.when(rs.getString("nome")).thenReturn("Maria");
        Mockito.when(rs.getString("cpf")).thenReturn("123");
        Mockito.when(rs.getString("sexo")).thenReturn("F");
        Mockito.when(rs.getString("nome_mae")).thenReturn("Ana");
        Mockito.when(rs.getString("nome_pai")).thenReturn("Jose");
        Mockito.when(rs.getObject("data_nascimento")).thenReturn(LocalDate.of(1990, 1, 1));
        Mockito.when(rs.getString("telefone")).thenReturn("6299");
        Mockito.when(rs.getString("keyword")).thenReturn("kw");
        Mockito.when(rs.getString("tipo_logradouro")).thenReturn("Rua");
        Mockito.when(rs.getString("logradouro")).thenReturn("A");
        Mockito.when(rs.getString("complemento")).thenReturn("Casa");
        Mockito.when(rs.getString("numero")).thenReturn("10");
        Mockito.when(rs.getString("cep")).thenReturn("70000");
        Mockito.when(rs.getString("bairro")).thenReturn("Centro");
        Mockito.when(rs.getLong("cidade_id")).thenReturn(12L);
        Mockito.when(rs.getString("cidade")).thenReturn("Goiania");
        Mockito.when(rs.getString("uf")).thenReturn("GO");

        PacienteDTO dto = mapper.detalhe().mapRow(rs, 1);

        Assertions.assertEquals("Maria", dto.nome());
        Assertions.assertEquals(LocalDate.of(1990, 1, 1), dto.dataNascimento());
        Assertions.assertEquals("Goiania", dto.endereco().cidade());
    }

    @Test
    void deveMapearResumoComDateSql() throws Exception {
        ResultSet rs = Mockito.mock(ResultSet.class);
        Mockito.when(rs.getLong("id")).thenReturn(1L);
        Mockito.when(rs.getString("nome")).thenReturn("Maria");
        Mockito.when(rs.getString("cpf")).thenReturn("123");
        Mockito.when(rs.getObject("data_nascimento")).thenReturn(java.sql.Date.valueOf("1990-01-01"));

        PacienteResumoDTO dto = mapper.resumo().mapRow(rs, 1);

        Assertions.assertEquals(LocalDate.of(1990, 1, 1), dto.dataNascimento());
    }

    @Test
    void deveMapearResumoComDataNula() throws Exception {
        ResultSet rs = Mockito.mock(ResultSet.class);
        Mockito.when(rs.getLong("id")).thenReturn(1L);
        Mockito.when(rs.getString("nome")).thenReturn("Maria");
        Mockito.when(rs.getString("cpf")).thenReturn("123");
        Mockito.when(rs.getObject("data_nascimento")).thenReturn(null);

        PacienteResumoDTO dto = mapper.resumo().mapRow(rs, 1);

        Assertions.assertNull(dto.dataNascimento());
    }

    @Test
    void deveConverterSexoCorretamente() throws Exception {
        ResultSet rs = Mockito.mock(ResultSet.class);
        Mockito.when(rs.getLong("id")).thenReturn(1L);
        Mockito.when(rs.getString("nome")).thenReturn("Maria");
        Mockito.when(rs.getString("cpf")).thenReturn("123");
        Mockito.when(rs.getString("sexo")).thenReturn("F");
        Mockito.when(rs.getString("nome_mae")).thenReturn("Ana");
        Mockito.when(rs.getString("nome_pai")).thenReturn("Jose");
        Mockito.when(rs.getObject("data_nascimento")).thenReturn(LocalDate.of(1990, 1, 1));
        Mockito.when(rs.getString("telefone")).thenReturn("6299");
        Mockito.when(rs.getString("keyword")).thenReturn("kw");
        Mockito.when(rs.getString("tipo_logradouro")).thenReturn("Rua");
        Mockito.when(rs.getString("logradouro")).thenReturn("A");
        Mockito.when(rs.getString("complemento")).thenReturn("Casa");
        Mockito.when(rs.getString("numero")).thenReturn("10");
        Mockito.when(rs.getString("cep")).thenReturn("70000");
        Mockito.when(rs.getString("bairro")).thenReturn("Centro");
        Mockito.when(rs.getLong("cidade_id")).thenReturn(12L);
        Mockito.when(rs.getString("cidade")).thenReturn("Goiania");
        Mockito.when(rs.getString("uf")).thenReturn("GO");

        PacienteDTO dtoF = mapper.detalhe().mapRow(rs, 1);
        Assertions.assertEquals("FEMININO", dtoF.sexo());

        Mockito.when(rs.getString("sexo")).thenReturn("M");
        PacienteDTO dtoM = mapper.detalhe().mapRow(rs, 1);
        Assertions.assertEquals("MASCULINO", dtoM.sexo());

        Mockito.when(rs.getString("sexo")).thenReturn("X");
        PacienteDTO dtoX = mapper.detalhe().mapRow(rs, 1);
        Assertions.assertEquals("-", dtoX.sexo());

        Mockito.when(rs.getString("sexo")).thenReturn(null);
        PacienteDTO dtoNull = mapper.detalhe().mapRow(rs, 1);
        Assertions.assertEquals("-", dtoNull.sexo());
    }
}
