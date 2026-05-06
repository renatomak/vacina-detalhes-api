package br.gov.saude.goiania.saude.api.exception;

import java.time.Instant;

public record ApiError(
        String message,
        Instant timestamp,
        String path
) {
}


