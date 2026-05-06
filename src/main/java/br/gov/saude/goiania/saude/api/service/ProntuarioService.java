package br.gov.saude.goiania.saude.api.service;

import br.gov.saude.goiania.saude.api.dto.ProntuarioEstruturadoResponse;

public interface ProntuarioService {
    ProntuarioEstruturadoResponse buscarProntuarioEstruturado(Long pacienteId);
}
