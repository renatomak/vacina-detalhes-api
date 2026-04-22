package br.gov.saude.vacinadetalhesapi.service;

import br.gov.saude.vacinadetalhesapi.dto.EnderecoDTO;
import br.gov.saude.vacinadetalhesapi.dto.PacienteDTO;
import br.gov.saude.vacinadetalhesapi.dto.PacienteResumoDTO;
import br.gov.saude.vacinadetalhesapi.repository.PacienteRepository;
import br.gov.saude.vacinadetalhesapi.service.impl.PacienteServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

class PacienteServiceImplTest {

    @Test
    void deveBuscarPorCpfComSucesso() {
        PacienteRepository repository = Mockito.mock(PacienteRepository.class);
        PacienteService service = new PacienteServiceImpl(repository, 50);
        PacienteDTO paciente = pacienteExemplo();
        Mockito.when(repository.buscarDetalhePorCpf("12345678901")).thenReturn(Optional.of(paciente));
        PacienteDTO retorno = service.buscarPorCpf("123.456.789-01");
        Assertions.assertEquals("Maria", retorno.nome());
        Mockito.verify(repository).buscarDetalhePorCpf("12345678901");
    }

    @Test
    void deveRetornar400QuandoBuscarPorCpfInvalido() {
        PacienteRepository repository = Mockito.mock(PacienteRepository.class);
        PacienteService service = new PacienteServiceImpl(repository, 50);
        ResponseStatusException ex = Assertions.assertThrows(ResponseStatusException.class, () -> service.buscarPorCpf("123"));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void deveRetornar404QuandoBuscarPorCpfNaoEncontrado() {
        PacienteRepository repository = Mockito.mock(PacienteRepository.class);
        PacienteService service = new PacienteServiceImpl(repository, 50);
        Mockito.when(repository.buscarDetalhePorCpf("00000000000")).thenReturn(Optional.empty());
        ResponseStatusException ex = Assertions.assertThrows(ResponseStatusException.class, () -> service.buscarPorCpf("00000000000"));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void deveBuscarPorNomeComSucesso() {
        PacienteRepository repository = Mockito.mock(PacienteRepository.class);
        PacienteService service = new PacienteServiceImpl(repository, 50);
        Mockito.when(repository.buscarResumoPorNome("Maria", 50)).thenReturn(List.of(new PacienteResumoDTO(1L, "Maria", "123", LocalDate.of(1990, 1, 1))));
        List<PacienteResumoDTO> lista = service.buscarPorNome("Maria");
        Assertions.assertEquals(1, lista.size());
        Assertions.assertEquals("Maria", lista.get(0).nome());
        Mockito.verify(repository).buscarResumoPorNome("Maria", 50);
    }

    @Test
    void deveRetornar400QuandoBuscarPorNomeVazio() {
        PacienteRepository repository = Mockito.mock(PacienteRepository.class);
        PacienteService service = new PacienteServiceImpl(repository, 50);
        ResponseStatusException ex = Assertions.assertThrows(ResponseStatusException.class, () -> service.buscarPorNome(" "));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void deveBuscarPorCpfQuandoQueryTem11Digitos() {
        PacienteRepository repository = Mockito.mock(PacienteRepository.class);
        PacienteService service = new PacienteServiceImpl(repository, 50);
        PacienteDTO paciente = pacienteExemplo();
        Mockito.when(repository.buscarDetalhePorCpf("12345678901")).thenReturn(Optional.of(paciente));
        PacienteSearchResult resultado = service.buscarPorQuery("123.456.789-01");
        Assertions.assertTrue(resultado.buscaPorCpf());
        Assertions.assertEquals(1L, resultado.paciente().id());
        Mockito.verify(repository).buscarDetalhePorCpf("12345678901");
    }

    @Test
    void deveBuscarPorNomeQuandoNaoForCpf() {
        PacienteRepository repository = Mockito.mock(PacienteRepository.class);
        PacienteService service = new PacienteServiceImpl(repository, 50);
        Mockito.when(repository.buscarResumoPorNome("maria", 50)).thenReturn(List.of(new PacienteResumoDTO(1L, "Maria", "123", LocalDate.of(1990, 1, 1))));
        PacienteSearchResult resultado = service.buscarPorQuery("maria");
        Assertions.assertFalse(resultado.buscaPorCpf());
        Assertions.assertEquals(1, resultado.pacientes().size());
        Mockito.verify(repository).buscarResumoPorNome("maria", 50);
    }

    @Test
    void deveRetornar400QuandoQueryForNula() {
        PacienteRepository repository = Mockito.mock(PacienteRepository.class);
        PacienteService service = new PacienteServiceImpl(repository, 50);
        ResponseStatusException ex = Assertions.assertThrows(ResponseStatusException.class, () -> service.buscarPorQuery(null));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void deveRetornar400QuandoQueryForVazia() {
        PacienteRepository repository = Mockito.mock(PacienteRepository.class);
        PacienteService service = new PacienteServiceImpl(repository, 50);
        ResponseStatusException ex = Assertions.assertThrows(ResponseStatusException.class, () -> service.buscarPorQuery("   "));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void deveRetornar404QuandoCpfNaoEncontradoNaQuery() {
        PacienteRepository repository = Mockito.mock(PacienteRepository.class);
        PacienteService service = new PacienteServiceImpl(repository, 50);
        Mockito.when(repository.buscarDetalhePorCpf("12345678901")).thenReturn(Optional.empty());
        ResponseStatusException ex = Assertions.assertThrows(ResponseStatusException.class, () -> service.buscarPorQuery("12345678901"));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void deveBuscarPorIdComSucesso() {
        PacienteRepository repository = Mockito.mock(PacienteRepository.class);
        PacienteService service = new PacienteServiceImpl(repository, 50);
        PacienteDTO paciente = pacienteExemplo();
        Mockito.when(repository.buscarDetalhePorId(1L)).thenReturn(Optional.of(paciente));
        PacienteDTO retorno = service.buscarPorId(1L);
        Assertions.assertEquals("Maria", retorno.nome());
    }

    @Test
    void deveRetornar404QuandoIdNaoEncontrado() {
        PacienteRepository repository = Mockito.mock(PacienteRepository.class);
        PacienteService service = new PacienteServiceImpl(repository, 50);
        Mockito.when(repository.buscarDetalhePorId(1L)).thenReturn(Optional.empty());
        ResponseStatusException ex = Assertions.assertThrows(ResponseStatusException.class, () -> service.buscarPorId(1L));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
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
                new EnderecoDTO(null, null, null, null, null, null, null, null, null, null)
        );
    }
}
