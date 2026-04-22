package br.gov.saude.vacinadetalhesapi.controller;

import br.gov.saude.vacinadetalhesapi.dto.EnderecoDTO;
import br.gov.saude.vacinadetalhesapi.dto.PacienteDTO;
import br.gov.saude.vacinadetalhesapi.dto.PacienteResumoDTO;
import br.gov.saude.vacinadetalhesapi.dto.VacinaDetalheDTO;
import br.gov.saude.vacinadetalhesapi.dto.VacinaResumoDTO;
import br.gov.saude.vacinadetalhesapi.service.PacienteSearchResult;
import br.gov.saude.vacinadetalhesapi.service.PacienteService;
import br.gov.saude.vacinadetalhesapi.service.VacinaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = "spring.datasource.hikari.initialization-fail-timeout=0")
@AutoConfigureMockMvc
@TestPropertySource(properties = "app.cors.allowed-origins=http://frontend.local")
class ApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PacienteService pacienteService;

    @MockBean
    private VacinaService vacinaService;

    @Test
    void deveBuscarPacientePorCpfViaEndpointSearch() throws Exception {
        when(pacienteService.buscarPorCpf(eq("12345678901")))
            .thenReturn(pacienteExemplo());

        mockMvc.perform(get("/api/pacientes/search/cpf").param("cpf", "12345678901"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nome").value("Maria"));
    }

    @Test
    void deveBuscarPacientesPorNomeViaEndpointSearch() throws Exception {
        when(pacienteService.buscarPorNome(eq("maria")))
            .thenReturn(List.of(new PacienteResumoDTO(1L, "Maria", "123", LocalDate.of(1990, 1, 1))));

        mockMvc.perform(get("/api/pacientes/search/nome").param("nome", "maria"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].nome").value("Maria"));
    }

    @Test
    void deveRetornarErroDeNegocioPadronizado() throws Exception {
        when(pacienteService.buscarPorId(404L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente nao encontrado"));

        mockMvc.perform(get("/api/pacientes/404"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Paciente nao encontrado"))
                .andExpect(jsonPath("$.path").value("/api/pacientes/404"));
    }

    @Test
    void deveRetornarErroInesperadoPadronizado() throws Exception {
        when(vacinaService.buscarDetalhePorAplicacaoId(anyLong()))
                .thenThrow(new RuntimeException("falha inesperada"));

        mockMvc.perform(get("/api/vacinas/aplicacoes/500"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Erro inesperado ao processar a requisicao."))
                .andExpect(jsonPath("$.path").value("/api/vacinas/aplicacoes/500"));
    }

    @Test
    void deveDetalharAplicacaoVacina() throws Exception {
        when(vacinaService.buscarDetalhePorAplicacaoId(1L)).thenReturn(vacinaDetalheExemplo());

        mockMvc.perform(get("/api/vacinas/aplicacoes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAplicacao").value(1))
                .andExpect(jsonPath("$.nomeVacina").value("Covid"));
    }

    @Test
    void deveListarVacinasDoPaciente() throws Exception {
        when(vacinaService.listarPorPacienteId(1L))
                .thenReturn(List.of(new VacinaResumoDTO(10L, LocalDate.of(2024, 1, 1), "Covid", "Reforco", "Adulto", "Aplicada")));

        mockMvc.perform(get("/api/pacientes/1/vacinas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idAplicacao").value(10));
    }

    @Test
    void devePermitirCorsParaOrigemConfigurada() throws Exception {
        mockMvc.perform(options("/api/pacientes/search")
                        .header("Origin", "http://frontend.local")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://frontend.local"));
    }

    @Test
    void deveNegarCorsParaOrigemNaoPermitida() throws Exception {
        mockMvc.perform(options("/api/pacientes/search")
                        .header("Origin", "http://origem-nao-permitida")
                        .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isForbidden());
    }

    private PacienteDTO pacienteExemplo() {
        return new PacienteDTO(
                1L,
                "Maria",
                "12345678901",
                "F",
                "Ana",
                "Jose",
                LocalDate.of(1990, 1, 1),
                "62999999999",
                new EnderecoDTO("kw", "Rua", "A", "Casa", "10", "70000", "Centro", 1L, "Goiania", "GO")
        );
    }

    private VacinaDetalheDTO vacinaDetalheExemplo() {
        return new VacinaDetalheDTO(
                1L, 100L, 1, "1a Dose", "Adulto", "Covid", "Comirnaty", "L123",
                LocalDate.of(2025, 1, 1), "Pfizer", "123", LocalDate.of(2024, 1, 1),
                "UBS", "Manha", "Grupo", "obs", "Aplicada", false, false,
                false, false, false, false, "IM", "Braco", "Joao", "CRM",
                "1234", "999", "UBS A", "1234567", "OK", "uuid"
        );
    }

    @Test
    void deveBuscarPacientePorCpfViaNovoEndpoint() throws Exception {
        when(pacienteService.buscarPorCpf(eq("12345678901")))
            .thenReturn(pacienteExemplo());

        mockMvc.perform(get("/api/pacientes/search/cpf").param("cpf", "12345678901"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.nome").value("Maria"));
    }

    @Test
    void deveBuscarPacientesPorNomeViaNovoEndpoint() throws Exception {
        when(pacienteService.buscarPorNome(eq("maria")))
            .thenReturn(List.of(new PacienteResumoDTO(1L, "Maria", "123", LocalDate.of(1990, 1, 1))));

        mockMvc.perform(get("/api/pacientes/search/nome").param("nome", "maria"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].nome").value("Maria"));
    }

    @Test
    void deveRetornar404QuandoBuscarPorCpfNaoEncontrado() throws Exception {
        when(pacienteService.buscarPorCpf(eq("00000000000")))
            .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Paciente nao encontrado para o CPF informado."));

        mockMvc.perform(get("/api/pacientes/search/cpf").param("cpf", "00000000000"))
            .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornar400QuandoBuscarPorCpfInvalido() throws Exception {
        when(pacienteService.buscarPorCpf(eq("123")))
            .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF invalido."));

        mockMvc.perform(get("/api/pacientes/search/cpf").param("cpf", "123"))
            .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar400QuandoBuscarPorNomeVazio() throws Exception {
        when(pacienteService.buscarPorNome(eq(" ")))
            .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "O parametro nome e obrigatorio."));

        mockMvc.perform(get("/api/pacientes/search/nome").param("nome", " "))
            .andExpect(status().isBadRequest());
    }
}


