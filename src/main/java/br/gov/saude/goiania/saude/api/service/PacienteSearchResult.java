package br.gov.saude.goiania.saude.api.service;

import br.gov.saude.goiania.saude.api.dto.PacienteDTO;
import br.gov.saude.goiania.saude.api.dto.PacienteResumoDTO;

import java.util.List;

public record PacienteSearchResult(
        boolean buscaPorCpf,
        PacienteDTO paciente,
        List<PacienteResumoDTO> pacientes
) {

    public static PacienteSearchResult resultadoCpf(PacienteDTO paciente) {
        return new PacienteSearchResult(true, paciente, List.of());
    }

    public static PacienteSearchResult resultadoNome(List<PacienteResumoDTO> pacientes) {
        return new PacienteSearchResult(false, null, pacientes);
    }
}


