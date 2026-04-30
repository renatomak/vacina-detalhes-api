package br.gov.saude.vacinadetalhesapi.service;

import br.gov.saude.vacinadetalhesapi.dto.VacinaDetalheDTO;
import br.gov.saude.vacinadetalhesapi.dto.VacinaResumoDTO;
import br.gov.saude.vacinadetalhesapi.repository.VacinaRepository;
import br.gov.saude.vacinadetalhesapi.service.impl.VacinaServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

class VacinaServiceImplTest {

    @Test
    void deveListarVacinasPorPaciente() {
        VacinaRepository repository = Mockito.mock(VacinaRepository.class);
        VacinaService service = new VacinaServiceImpl(repository);

        Mockito.when(repository.listarPorPacienteId(10L))
                .thenReturn(List.of(new VacinaResumoDTO(1L, LocalDate.of(2024, 1, 10), "Covid", "Reforco", "Adulto", "Pfizer", "UBS Central", "Joao Profissional")));

        List<VacinaResumoDTO> vacinas = service.listarPorPacienteId(10L);

        Assertions.assertEquals(1, vacinas.size());
        Assertions.assertEquals("Covid", vacinas.get(0).vacina());
    }

    @Test
    void deveBuscarDetalhePorAplicacaoId() {
        VacinaRepository repository = Mockito.mock(VacinaRepository.class);
        VacinaService service = new VacinaServiceImpl(repository);

        VacinaDetalheDTO detalhe = new VacinaDetalheDTO(
                1L, 100L, 1, "1a Dose", "Adulto", "Covid", "Comirnaty", "L123",
                LocalDate.of(2025, 1, 1), "Pfizer", "123", LocalDate.of(2024, 1, 1),
                "UBS", "Manha", "Grupo", "obs", "Aplicada", false, false,
                false, false, false, false, "IM", "Braco", "Joao", "CRM",
                "1234", "999", "UBS A", "1234567", "OK", "uuid"
        );

        Mockito.when(repository.buscarDetalhePorAplicacaoId(1L)).thenReturn(Optional.of(detalhe));

        VacinaDetalheDTO retorno = service.buscarDetalhePorAplicacaoId(1L);

        Assertions.assertEquals(1L, retorno.idAplicacao());
        Assertions.assertEquals("Covid", retorno.nomeVacina());
    }

    @Test
    void deveRetornar404QuandoAplicacaoNaoEncontrada() {
        VacinaRepository repository = Mockito.mock(VacinaRepository.class);
        VacinaService service = new VacinaServiceImpl(repository);

        Mockito.when(repository.buscarDetalhePorAplicacaoId(99L)).thenReturn(Optional.empty());

        ResponseStatusException ex = Assertions.assertThrows(ResponseStatusException.class,
                () -> service.buscarDetalhePorAplicacaoId(99L));

        Assertions.assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }
}
