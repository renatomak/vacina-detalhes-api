package br.gov.saude.vacinadetalhesapi.dto;

import java.util.List;

public record AtendimentoDTO(
    Long numeroAtendimento,
    String dataChegada,
    UnidadeDTO unidade,
    String tipoAtendimento,
    ProfissionalDTO profissional,
    String classificacaoRisco,
    List<RegistroDTO> registros
) {}
