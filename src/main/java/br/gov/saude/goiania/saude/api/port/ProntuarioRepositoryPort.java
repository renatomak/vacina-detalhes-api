package br.gov.saude.goiania.saude.api.port;

import br.gov.saude.goiania.saude.api.dto.AtendimentoDTO;
import java.util.List;

public interface ProntuarioRepositoryPort {
    List<AtendimentoDTO> buscarAtendimentosComRegistrosPorPaciente(Long pacienteId);
}
