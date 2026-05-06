package br.gov.saude.goiania.saude.api;

import br.gov.saude.goiania.saude.api.dto.EnderecoDTO;
import br.gov.saude.goiania.saude.api.dto.PacienteDTO;
import br.gov.saude.goiania.saude.api.dto.PacienteResumoDTO;
import br.gov.saude.goiania.saude.api.dto.VacinaAplicacaoDTO;
import br.gov.saude.goiania.saude.api.dto.VacinaDetalheDTO;
import br.gov.saude.goiania.saude.api.dto.VacinaResumoDTO;
import br.gov.saude.goiania.saude.api.exception.BadRequestException;
import br.gov.saude.goiania.saude.api.exception.ResourceNotFoundException;
import br.gov.saude.goiania.saude.api.service.PacienteSearchResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

class CoreClassesCoverageTest {

    @Test
    void deveCobrirFactoriesDePacienteSearchResult() {
        PacienteDTO paciente = new PacienteDTO(
            1L, "Maria", "123", "F", "Ana", "Jose", "28/04/2026", "62",
            "123456789012345", "Maria Social", "Brasil", "GO", "Goiania", "Branca", "Nenhuma", "62988887777", "maria@email.com", null, "Brasil"
        );
        PacienteResumoDTO resumo = new PacienteResumoDTO(1L, "Maria", "123", LocalDate.now());

        PacienteSearchResult porCpf = PacienteSearchResult.resultadoCpf(paciente);
        PacienteSearchResult porNome = PacienteSearchResult.resultadoNome(List.of(resumo));

        Assertions.assertTrue(porCpf.buscaPorCpf());
        Assertions.assertFalse(porNome.buscaPorCpf());
    }

    @Test
    void deveCobrirRecordsEExceptions() {
        EnderecoDTO endereco = new EnderecoDTO("kw", "Rua", "A", "Casa", "10", "70000", "Centro", 1L, "Goiania", "GO");
        PacienteDTO paciente = new PacienteDTO(
            1L, "Maria", "123", "F", "Ana", "Jose", "01/01/1990", "62",
            "123456789012345", "Maria Social", "Brasil", "GO", "Goiania", "Branca", "Nenhuma", "62988887777", "maria@email.com", endereco, "Brasil"
        );
        PacienteResumoDTO pacienteResumo = new PacienteResumoDTO(1L, "Maria", "123", LocalDate.of(1990, 1, 1));
        VacinaResumoDTO vacinaResumo = new VacinaResumoDTO(1L, LocalDate.of(2024, 1, 1), "Covid", "Reforco", "Adulto", "Pfizer", "UBS Central", "Joao Profissional");
        VacinaDetalheDTO vacinaDetalhe = new VacinaDetalheDTO(
                1L, 1L, 1, "1a Dose", "Adulto", "Covid", "Comirnaty", "L123",
                LocalDate.of(2025, 1, 1), "Pfizer", "123", LocalDate.of(2024, 1, 1),
                "UBS", "Manha", "Grupo", "obs", "Aplicada", false, false,
                false, false, false, false, "IM", "Braco", "Joao", "CRM",
                "1234", "999", "UBS A", "1234567", "OK", "uuid"
        );
        VacinaAplicacaoDTO vacinaAplicacao = new VacinaAplicacaoDTO(
                1L, "1", "Covid", "Comirnaty", "1a Dose", "Adulto", "L123", "2025-01-01", "Pfizer",
                "123", "2024-01-01", "Joao", "CRM", "1234", "999", "UBS A", "1234567",
                "UBS", "Manha", "Grupo", "N", "N", "N", "N", "N", "N", "IM", "Braco", "obs",
                "Aplicada", "100", "OK", "uuid"
        );

        BadRequestException badRequest = new BadRequestException("erro");
        ResourceNotFoundException notFound = new ResourceNotFoundException("nao encontrado");

        Assertions.assertEquals("Goiania", paciente.endereco().cidade());
        Assertions.assertEquals("Maria", pacienteResumo.nome());
        Assertions.assertEquals("Covid", vacinaResumo.vacina());
        Assertions.assertEquals("uuid", vacinaDetalhe.rndsUuid());
        Assertions.assertEquals("uuid", vacinaAplicacao.uuidRnds());
        Assertions.assertEquals("erro", badRequest.getMessage());
        Assertions.assertEquals("nao encontrado", notFound.getMessage());
    }
}
