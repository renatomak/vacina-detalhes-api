package br.gov.saude.vacinadetalhesapi.port;

import br.gov.saude.vacinadetalhesapi.domain.ProntuarioItem;
import java.util.List;

public interface ProntuarioRepositoryPort {
    List<ProntuarioItem> buscarHistoricoPorPaciente(Long pacienteId);
}

