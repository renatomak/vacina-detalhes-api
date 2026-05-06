package br.gov.saude.goiania.saude.api.dto;

import java.util.List;

public record ExameResponseDTO(String grupo, List<String> itens) {}

