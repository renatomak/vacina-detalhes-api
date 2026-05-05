package br.gov.saude.vacinadetalhesapi.service;

import br.gov.saude.vacinadetalhesapi.dto.PagedResponse;
import br.gov.saude.vacinadetalhesapi.dto.RaasPsiArquivoDTO;

import java.time.LocalDate;

public interface RaasPsiService {

    PagedResponse<RaasPsiArquivoDTO> listarArquivos(LocalDate competencia,
                                                    Long unidadeId,
                                                    String unidade,
                                                    String situacao,
                                                    int page,
                                                    int size);
}
