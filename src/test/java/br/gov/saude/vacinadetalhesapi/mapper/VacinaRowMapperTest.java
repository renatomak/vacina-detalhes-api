package br.gov.saude.vacinadetalhesapi.mapper;

import br.gov.saude.vacinadetalhesapi.dto.VacinaDetalheDTO;
import br.gov.saude.vacinadetalhesapi.dto.VacinaResumoDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mapstruct.factory.Mappers;

import java.sql.ResultSet;
import java.time.LocalDate;

class VacinaRowMapperTest {
    private final VacinaRawRowMapper rawRowMapper = new VacinaRawRowMapper();
    private final VacinaMapper vacinaMapper = Mappers.getMapper(VacinaMapper.class);

    @Test
    void deveMapearResumoComDateSql() throws Exception {
        ResultSet rs = Mockito.mock(ResultSet.class);
        Mockito.when(rs.getLong("id_aplicacao")).thenReturn(10L);
        Mockito.when(rs.getObject("data_aplicacao")).thenReturn(java.sql.Date.valueOf("2024-05-10"));
        Mockito.when(rs.getString("nome_vacina")).thenReturn("Influenza");
        Mockito.when(rs.getString("dose")).thenReturn("Reforco");
        Mockito.when(rs.getString("estrategia")).thenReturn("Campanha");
        Mockito.when(rs.getString("status")).thenReturn("Aplicada");

        VacinaRaw raw = rawRowMapper.resumo().mapRow(rs, 1);
        VacinaResumoDTO dto = vacinaMapper.toVacinaResumoDTO(raw);

        Assertions.assertEquals(10L, dto.idAplicacao());
        Assertions.assertEquals(LocalDate.of(2024, 5, 10), dto.dataAplicacao());
    }

    @Test
    void deveMapearDetalheComConversoesNulas() throws Exception {
        ResultSet rs = Mockito.mock(ResultSet.class);
        Mockito.when(rs.getLong("id_aplicacao")).thenReturn(1L);
        Mockito.when(rs.getObject("nr_atendimento")).thenReturn(null);
        Mockito.when(rs.getObject("dose_codigo")).thenReturn(null);
        Mockito.when(rs.getString("dose")).thenReturn("1a Dose");
        Mockito.when(rs.getString("estrategia")).thenReturn("Rotina");
        Mockito.when(rs.getString("nome_vacina")).thenReturn("Covid");
        Mockito.when(rs.getString("descricao_vacina")).thenReturn("Comirnaty");
        Mockito.when(rs.getString("lote")).thenReturn("L1");
        Mockito.when(rs.getObject("validade_lote")).thenReturn(LocalDate.of(2025, 1, 1));
        Mockito.when(rs.getString("fabricante_nome")).thenReturn("Pfizer");
        Mockito.when(rs.getString("fabricante_cnpj")).thenReturn("123");
        Mockito.when(rs.getObject("data_aplicacao")).thenReturn(LocalDate.of(2024, 1, 1));
        Mockito.when(rs.getString("local_atendimento")).thenReturn("UBS");
        Mockito.when(rs.getString("turno")).thenReturn("Manha");
        Mockito.when(rs.getString("grupo_atendimento")).thenReturn("A");
        Mockito.when(rs.getString("observacao")).thenReturn("Obs");
        Mockito.when(rs.getString("status")).thenReturn("Aplicada");
        Mockito.when(rs.getBoolean("gestante")).thenReturn(false);
        Mockito.when(rs.getBoolean("puerpera")).thenReturn(false);
        Mockito.when(rs.getBoolean("historico")).thenReturn(false);
        Mockito.when(rs.getBoolean("fora_esquema")).thenReturn(false);
        Mockito.when(rs.getBoolean("viajante")).thenReturn(false);
        Mockito.when(rs.getBoolean("novo_frasco")).thenReturn(false);
        Mockito.when(rs.getString("via_administracao")).thenReturn("IM");
        Mockito.when(rs.getString("local_aplicacao")).thenReturn("Braco");
        Mockito.when(rs.getString("profissional_nome")).thenReturn("Joao");
        Mockito.when(rs.getString("profissional_conselho")).thenReturn("CRM");
        Mockito.when(rs.getString("profissional_registro")).thenReturn("123");
        Mockito.when(rs.getString("profissional_cns")).thenReturn("999");
        Mockito.when(rs.getString("unidade_nome")).thenReturn("UBS X");
        Mockito.when(rs.getString("unidade_cnes")).thenReturn("1234567");
        Mockito.when(rs.getString("rnds_situacao")).thenReturn("OK");
        Mockito.when(rs.getString("rnds_uuid")).thenReturn("uuid");

        VacinaRaw raw = rawRowMapper.detalhe().mapRow(rs, 1);
        VacinaDetalheDTO dto = vacinaMapper.toVacinaDetalheDTO(raw);

        Assertions.assertNull(dto.nrAtendimento());
        Assertions.assertNull(dto.doseCodigo());
        Assertions.assertEquals("Covid", dto.nomeVacina());
    }

    @Test
    void deveMapearDetalheComConversoesNumericas() throws Exception {
        ResultSet rs = Mockito.mock(ResultSet.class);
        Mockito.when(rs.getLong("id_aplicacao")).thenReturn(1L);
        Mockito.when(rs.getObject("nr_atendimento")).thenReturn(200);
        Mockito.when(rs.getObject("dose_codigo")).thenReturn(2L);
        Mockito.when(rs.getString(Mockito.anyString())).thenReturn("x");
        Mockito.when(rs.getObject("validade_lote")).thenReturn(java.sql.Date.valueOf("2025-01-01"));
        Mockito.when(rs.getObject("data_aplicacao")).thenReturn(java.sql.Date.valueOf("2024-01-01"));
        Mockito.when(rs.getBoolean(Mockito.anyString())).thenReturn(true);

        VacinaRaw raw = rawRowMapper.detalhe().mapRow(rs, 1);
        VacinaDetalheDTO dto = vacinaMapper.toVacinaDetalheDTO(raw);

        Assertions.assertEquals(200L, dto.nrAtendimento());
        Assertions.assertEquals(2, dto.doseCodigo());
        Assertions.assertEquals(LocalDate.of(2024, 1, 1), dto.dataAplicacao());
    }
}


