package br.gov.saude.vacinadetalhesapi.port;

import br.gov.saude.vacinadetalhesapi.dto.AtendimentoDTO;
import java.util.List;

public interface ProntuarioRepositoryPort {
    List<AtendimentoDTO> buscarAtendimentosComRegistrosPorPaciente(Long pacienteId);
}
