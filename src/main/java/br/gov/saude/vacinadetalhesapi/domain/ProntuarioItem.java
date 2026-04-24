package br.gov.saude.vacinadetalhesapi.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public record ProntuarioItem(
        String dataRegistro,
        String profissional,
        String unidade,
        String tipoRegistro,
        String classificacaoRisco,
        String conteudo
) {
    public ProntuarioItem {
        Objects.requireNonNull(dataRegistro);
        Objects.requireNonNull(profissional);
        Objects.requireNonNull(unidade);
        Objects.requireNonNull(tipoRegistro);
        Objects.requireNonNull(classificacaoRisco);
        Objects.requireNonNull(conteudo);
    }
}

