package br.gov.saude.vacinadetalhesapi.dto;

public record RegistroDTO(
    String data,
    String tipo,
    ConteudoDTO conteudo
) {}
