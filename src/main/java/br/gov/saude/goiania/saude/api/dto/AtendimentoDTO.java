package br.gov.saude.goiania.saude.api.dto;

import java.util.List;

public record AtendimentoDTO(
    Long numeroAtendimento,
    String dataChegada,
    UnidadeDTO unidade,
    String tipoAtendimento,
    ProfissionalDTO profissional,
    String classificacaoRisco,
    Boolean possuiAih,
    AihDetalhesDTO aihDetalhes,
    List<RegistroDTO> registros
) {}
