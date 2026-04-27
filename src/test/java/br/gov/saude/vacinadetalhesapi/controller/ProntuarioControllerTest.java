package br.gov.saude.vacinadetalhesapi.controller;

import br.gov.saude.vacinadetalhesapi.domain.ProntuarioItem;
import br.gov.saude.vacinadetalhesapi.dto.PacienteDTO;
import br.gov.saude.vacinadetalhesapi.dto.ProntuarioResponse;
import br.gov.saude.vacinadetalhesapi.service.ProntuarioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProntuarioControllerTest {
    @Test
    void deveRetornarHistoricoDoPaciente() {
        ProntuarioService prontuarioService = mock(ProntuarioService.class);
        ProntuarioItem item = new ProntuarioItem("01/01/2024 10:00:00", "Profissional", "Unidade", "Evolução", "Baixo", "Conteudo");
        PacienteDTO paciente = new PacienteDTO(1L, "Nome", "CPF", "FEMININO", "Mae", null, java.time.LocalDate.of(2024, 6, 30), "(62) 99870-2201", null);
        ProntuarioResponse prontuarioResponse = new ProntuarioResponse(paciente, List.of(item));
        when(prontuarioService.buscarProntuarioCompleto(1L)).thenReturn(prontuarioResponse);
        ProntuarioController controller = new ProntuarioController(prontuarioService);
        ResponseEntity<ProntuarioResponse> response = controller.buscarHistoricoPorPaciente(1L);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getAtendimentos()).hasSize(1);
        assertThat(response.getBody().getAtendimentos().get(0).conteudo()).isEqualTo("Conteudo");
        assertThat(response.getBody().getPaciente()).isNotNull();
        assertThat(response.getBody().getPaciente().id()).isEqualTo(1L);
    }
}
