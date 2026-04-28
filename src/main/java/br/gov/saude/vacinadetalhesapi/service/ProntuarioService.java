package br.gov.saude.vacinadetalhesapi.service;

import br.gov.saude.vacinadetalhesapi.dto.ProntuarioEstruturadoResponse;

public interface ProntuarioService {
    ProntuarioEstruturadoResponse buscarProntuarioEstruturado(Long pacienteId);
}
