package br.gov.saude.vacinadetalhesapi.dto;

import java.util.List;

public record ProntuarioDTO(
    Long pacienteId,
    List<AtendimentoDTO> atendimentos
) {}

