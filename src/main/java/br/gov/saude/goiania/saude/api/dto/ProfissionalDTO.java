package br.gov.saude.goiania.saude.api.dto;

public record ProfissionalDTO(
    String nome,
    String registro,
    String tipoConselho,
    String cbo,
    String cboDescricao
) {}

