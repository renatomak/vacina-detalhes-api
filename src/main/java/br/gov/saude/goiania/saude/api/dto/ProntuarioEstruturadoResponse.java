package br.gov.saude.goiania.saude.api.dto;

import java.util.List;

public record ProntuarioEstruturadoResponse(
    PacienteDTO paciente,
    List<AtendimentoDTO> atendimentos
) {}

