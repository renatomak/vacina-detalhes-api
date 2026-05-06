package br.gov.saude.goiania.saude.api.dto;

import java.util.List;

public record ProntuarioDTO(
    Long pacienteId,
    List<AtendimentoDTO> atendimentos
) {}

