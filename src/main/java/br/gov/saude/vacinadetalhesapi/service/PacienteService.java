package br.gov.saude.vacinadetalhesapi.service;

import br.gov.saude.vacinadetalhesapi.dto.PacienteDTO;

public interface PacienteService {

    PacienteSearchResult buscarPorQuery(String query);

    PacienteDTO buscarPorId(Long id);
}


