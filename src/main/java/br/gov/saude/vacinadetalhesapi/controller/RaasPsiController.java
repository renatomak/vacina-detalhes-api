package br.gov.saude.vacinadetalhesapi.controller;

import br.gov.saude.vacinadetalhesapi.dto.PagedResponse;
import br.gov.saude.vacinadetalhesapi.dto.RaasPsiArquivoDTO;
import br.gov.saude.vacinadetalhesapi.dto.RaasPsiArquivoResponseDTO;
import br.gov.saude.vacinadetalhesapi.service.RaasPsiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/raas-psi")
@Tag(name = "RAAS PSI", description = "Gestão de arquivos RAAS PSI gerados")
public class RaasPsiController {

    private static final Logger logger = LoggerFactory.getLogger(RaasPsiController.class);

    private final RaasPsiService raasPsiService;

    public RaasPsiController(RaasPsiService raasPsiService) {
        this.raasPsiService = raasPsiService;
    }

    /**
     * Lista os arquivos RAAS PSI gerados, agrupados por competência e unidade.
     *
     * @param competencia  Competência no formato {@code YYYY-MM} ou {@code YYYY-MM-DD} (opcional).
     * @param unidadeId    ID da unidade prestadora (opcional).
     * @param unidade      Nome da unidade prestadora (opcional).
     * @param situacao     {@code "Arquivo Gerado"} ou {@code "Sem Registros"} (opcional).
     * @param page         Número da página (0-based), padrão 0.
     * @param size         Tamanho da página, padrão 20.
     */
    @GetMapping("/arquivos")
    @Operation(summary = "Listar arquivos RAAS PSI",
               description = "Retorna os arquivos RAAS PSI agrupados por competência e unidade, com suporte a filtros e paginação.")
    public ResponseEntity<PagedResponse<RaasPsiArquivoResponseDTO>> listarArquivos(
            @Parameter(description = "Competência no formato YYYY-MM ou YYYY-MM-DD")
            @RequestParam(required = false) String competencia,

            @Parameter(description = "ID da unidade prestadora")
            @RequestParam(name = "unidade_id", required = false) Long unidadeId,

            @Parameter(description = "Nome da unidade prestadora")
            @RequestParam(required = false) String unidade,

            @Parameter(description = "Situação: 'Arquivo Gerado' ou 'Sem Registros'")
            @RequestParam(required = false) String situacao,

            @Parameter(description = "Número da página (0-based)")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Tamanho da página")
            @RequestParam(defaultValue = "20") int size) {
        try {
            LocalDate competenciaDate = parseCompetencia(competencia);
            PagedResponse<RaasPsiArquivoDTO> response =
                    raasPsiService.listarArquivos(competenciaDate, unidadeId, unidade, situacao, page, size);
            // Converte para DTO de resposta com data formatada
            PagedResponse<RaasPsiArquivoResponseDTO> responseFormatado = new PagedResponse<>(
                response.content().stream().map(RaasPsiArquivoResponseDTO::from).collect(Collectors.toList()),
                response.page(),
                response.size(),
                response.totalElements(),
                response.totalPages()
            );
            return ResponseEntity.ok(responseFormatado);
        } catch (Exception ex) {
            logger.error("Erro inesperado ao processar a requisição de arquivos RAAS PSI", ex);
            return ResponseEntity.status(500).body(null);
        }
    }

    // -------------------------------------------------------------------------
    // helpers
    // -------------------------------------------------------------------------

    /**
     * Aceita formatos {@code YYYY-MM} (normaliza para o primeiro dia do mês)
     * ou {@code YYYY-MM-DD}.
     */
    private LocalDate parseCompetencia(String competencia) {
        if (competencia == null || competencia.isBlank()) {
            return null;
        }
        // Tenta YYYY-MM
        if (competencia.matches("\\d{4}-\\d{2}")) {
            YearMonth ym = YearMonth.parse(competencia);
            return ym.atDay(1);
        }
        // Tenta YYYY-MM-DD
        try {
            return LocalDate.parse(competencia);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "Formato de competência inválido: '" + competencia +
                    "'. Use YYYY-MM ou YYYY-MM-DD.");
        }
    }
}
