package br.gov.saude.goiania.saude.api.service;

import br.gov.saude.goiania.saude.api.dto.PagedResponse;
import br.gov.saude.goiania.saude.api.dto.RaasPsiArquivoDTO;

import java.time.LocalDate;

public interface RaasPsiService {

    PagedResponse<RaasPsiArquivoDTO> listarArquivos(LocalDate competencia,
                                                    Long unidadeId,
                                                    String unidade,
                                                    String situacao,
                                                    int page,
                                                    int size);
}
