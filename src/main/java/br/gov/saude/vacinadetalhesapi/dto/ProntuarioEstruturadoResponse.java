package br.gov.saude.vacinadetalhesapi.dto;

import java.util.List;

public record ProntuarioEstruturadoResponse(
    PacienteDTO paciente,
    List<AtendimentoDTO> atendimentos
) {}

