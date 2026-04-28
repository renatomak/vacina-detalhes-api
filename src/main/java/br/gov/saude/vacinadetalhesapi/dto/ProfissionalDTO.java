package br.gov.saude.vacinadetalhesapi.dto;

public record ProfissionalDTO(
    String nome,
    String registro,
    String tipoConselho,
    String cbo,
    String cboDescricao
) {}

