package br.gov.saude.vacinadetalhesapi.service;

import br.gov.saude.vacinadetalhesapi.domain.ProntuarioItem;
import br.gov.saude.vacinadetalhesapi.dto.ProntuarioResponse;
import java.util.List;

public interface ProntuarioService {
    List<ProntuarioItem> buscarHistoricoPorPaciente(Long pacienteId);
    ProntuarioResponse buscarProntuarioCompleto(Long pacienteId);
}
