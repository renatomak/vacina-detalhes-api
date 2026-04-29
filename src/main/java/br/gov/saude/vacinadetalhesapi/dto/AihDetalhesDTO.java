package br.gov.saude.vacinadetalhesapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AihDetalhesDTO(
    String dataCadastro,
    String principaisSinais,
    String condicoesInternacao,
    String principaisResultados,
    String diagnosticoInicial
) {}
