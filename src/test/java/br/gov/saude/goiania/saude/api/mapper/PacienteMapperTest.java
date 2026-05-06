package br.gov.saude.goiania.saude.api.mapper;

import br.gov.saude.goiania.saude.api.dto.PacienteDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PacienteMapperTest {
    @Test
    void testToPacienteDTO_sexoFeminino() {
        PacienteRaw raw = new PacienteRaw();
        raw.id = 1L;
        raw.nome = "Maria";
        raw.cpf = "123";
        raw.sexo = "F";
        raw.nomeMae = "Ana";
        raw.nomePai = "Jose";
        raw.dataNascimento = LocalDate.of(1990, 1, 1);
        raw.telefone = "6299";
        raw.keyword = "kw";
        raw.tipoLogradouro = "Rua";
        raw.logradouro = "A";
        raw.complemento = "Casa";
        raw.numero = "10";
        raw.cep = "70000";
        raw.bairro = "Centro";
        raw.cidadeId = 12L;
        raw.cidade = "Goiania";
        raw.uf = "GO";

        PacienteDTO dto = PacienteMapper.INSTANCE.toPacienteDTO(raw);
        assertEquals("FEMININO", dto.sexo());
        assertEquals("Maria", dto.nome());
        assertEquals("Goiania", dto.endereco().cidade());
    }

    @Test
    void testToPacienteDTO_sexoMasculino() {
        PacienteRaw raw = new PacienteRaw();
        raw.id = 2L;
        raw.nome = "Joao";
        raw.cpf = "456";
        raw.sexo = "M";
        raw.nomeMae = "Ana";
        raw.nomePai = "Jose";
        raw.dataNascimento = LocalDate.of(1985, 5, 10);
        raw.telefone = "6298";
        raw.keyword = "kw";
        raw.tipoLogradouro = "Rua";
        raw.logradouro = "B";
        raw.complemento = "Apto";
        raw.numero = "20";
        raw.cep = "70001";
        raw.bairro = "Setor";
        raw.cidadeId = 13L;
        raw.cidade = "Anapolis";
        raw.uf = "GO";

        PacienteDTO dto = PacienteMapper.INSTANCE.toPacienteDTO(raw);
        assertEquals("MASCULINO", dto.sexo());
        assertEquals("Joao", dto.nome());
        assertEquals("Anapolis", dto.endereco().cidade());
    }

    @Test
    void testToPacienteDTO_sexoOutro() {
        PacienteRaw raw = new PacienteRaw();
        raw.id = 3L;
        raw.nome = "Alex";
        raw.cpf = "789";
        raw.sexo = "X";
        raw.nomeMae = "Ana";
        raw.nomePai = "Jose";
        raw.dataNascimento = LocalDate.of(2000, 12, 31);
        raw.telefone = "6297";
        raw.keyword = "kw";
        raw.tipoLogradouro = "Avenida";
        raw.logradouro = "C";
        raw.complemento = "Bloco";
        raw.numero = "30";
        raw.cep = "70002";
        raw.bairro = "Bairro";
        raw.cidadeId = 14L;
        raw.cidade = "Trindade";
        raw.uf = "GO";

        PacienteDTO dto = PacienteMapper.INSTANCE.toPacienteDTO(raw);
        assertEquals("-", dto.sexo());
        assertEquals("Alex", dto.nome());
        assertEquals("Trindade", dto.endereco().cidade());
    }
}

