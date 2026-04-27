package br.gov.saude.vacinadetalhesapi.service.impl;

import br.gov.saude.vacinadetalhesapi.domain.ProntuarioItem;
import br.gov.saude.vacinadetalhesapi.port.ProntuarioRepositoryPort;
import br.gov.saude.vacinadetalhesapi.service.PacienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProntuarioServiceImplTest {
    private ProntuarioRepositoryPort prontuarioRepositoryPort;
    private PacienteService pacienteService;
    private ProntuarioServiceImpl prontuarioService;

    @BeforeEach
    void setUp() {
        prontuarioRepositoryPort = mock(ProntuarioRepositoryPort.class);
        pacienteService = mock(PacienteService.class);
        prontuarioService = new ProntuarioServiceImpl(prontuarioRepositoryPort, pacienteService);
    }

    @Test
    void deveRetornarConteudoSemHtml() {
        ProntuarioItem itemComHtml = new ProntuarioItem("01/01/2024 10:00:00", "Profissional", "Unidade", "Evolução", "Baixo", "<b>Texto</b> com <i>HTML</i>");
        when(prontuarioRepositoryPort.buscarHistoricoPorPaciente(1L)).thenReturn(List.of(itemComHtml));
        List<ProntuarioItem> result = prontuarioService.buscarHistoricoPorPaciente(1L);
        assertThat(result).hasSize(1);
        assertThat(result.get(0).conteudo()).isEqualTo("Texto com HTML");
    }
}
