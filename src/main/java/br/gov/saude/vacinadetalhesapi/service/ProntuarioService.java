package br.gov.saude.vacinadetalhesapi.service;

import br.gov.saude.vacinadetalhesapi.domain.ProntuarioItem;
import java.util.List;

public interface ProntuarioService {
    List<ProntuarioItem> buscarHistoricoPorPaciente(Long pacienteId);
}

