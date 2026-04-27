package br.gov.saude.vacinadetalhesapi.dto;

import java.util.List;

public record ExameResponseDTO(String grupo, List<String> itens) {}

