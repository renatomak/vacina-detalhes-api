package br.gov.saude.goiania.saude.api.service.impl;

import br.gov.saude.goiania.saude.api.dto.PagedResponse;
import br.gov.saude.goiania.saude.api.dto.RaasPsiArquivoDTO;
import br.gov.saude.goiania.saude.api.repository.RaasPsiRepository;
import br.gov.saude.goiania.saude.api.service.RaasPsiService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RaasPsiServiceImpl implements RaasPsiService {

    private static final String SITUACAO_ARQUIVO_GERADO = "Arquivo Gerado";
    private static final String SITUACAO_SEM_REGISTROS  = "Sem Registros";

    private final RaasPsiRepository raasPsiRepository;

    public RaasPsiServiceImpl(RaasPsiRepository raasPsiRepository) {
        this.raasPsiRepository = raasPsiRepository;
    }

    @Override
    public PagedResponse<RaasPsiArquivoDTO> listarArquivos(LocalDate competencia,
                                                           Long unidadeId,
                                                           String unidade,
                                                           String situacao,
                                                           int page,
                                                           int size) {

        // Se o filtro de situação é "Sem Registros", verifica se há dados
        if (SITUACAO_SEM_REGISTROS.equalsIgnoreCase(situacao)) {
            long total = raasPsiRepository.contarArquivos(competencia, unidadeId, unidade);
            if (total == 0) {
                return PagedResponse.of(List.of(semRegistrosEntry(competencia, unidadeId, unidade)), 0, size, 1);
            }
            // Se há dados, o filtro "Sem Registros" não retorna resultados
            return PagedResponse.of(List.of(), page, size, 0);
        }

        long total = raasPsiRepository.contarArquivos(competencia, unidadeId, unidade);

        // Sem registros no banco para o filtro informado
        if (total == 0) {
            // Só sintetiza "Sem Registros" se o filtro de situação não for "Arquivo Gerado"
            if (!SITUACAO_ARQUIVO_GERADO.equalsIgnoreCase(situacao)) {
                return PagedResponse.of(List.of(semRegistrosEntry(competencia, unidadeId, unidade)), 0, size, 1);
            }
            return PagedResponse.of(List.of(), page, size, 0);
        }

        List<RaasPsiArquivoDTO> content = raasPsiRepository.listarArquivos(competencia, unidadeId, unidade, page, size);
        return PagedResponse.of(content, page, size, total);
    }

    // -------------------------------------------------------------------------
    // helpers
    // -------------------------------------------------------------------------

    private RaasPsiArquivoDTO semRegistrosEntry(LocalDate competencia, Long unidadeId, String unidade) {
        String mesNome = competencia != null
                ? competencia.getMonth().getDisplayName(
                        java.time.format.TextStyle.FULL, new java.util.Locale("pt", "BR"))
                : null;
        if (mesNome != null) {
            mesNome = mesNome.substring(0, 1).toUpperCase() + mesNome.substring(1);
        }
        int ano = competencia != null ? competencia.getYear() : 0;
        return new RaasPsiArquivoDTO(
                mesNome,
                ano,
                LocalDate.now(),
                unidadeId,
                unidade,
                SITUACAO_SEM_REGISTROS,
                0L
        );
    }
}
