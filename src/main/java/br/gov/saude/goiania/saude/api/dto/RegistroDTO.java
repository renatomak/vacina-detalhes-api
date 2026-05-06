package br.gov.saude.goiania.saude.api.dto;

public record RegistroDTO(
    String data,
    String tipo,
    ConteudoDTO conteudo
) {}
