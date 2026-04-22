package br.gov.saude.vacinadetalhesapi.dto;

import java.time.LocalDate;

public record VacinaResumoDTO(
        Long idAplicacao,
        LocalDate dataAplicacao,
        String nomeVacina,
        String dose,
        String estrategia,
        String status
) {
}


