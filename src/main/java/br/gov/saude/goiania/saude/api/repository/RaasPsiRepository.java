package br.gov.saude.goiania.saude.api.repository;

import br.gov.saude.goiania.saude.api.dto.RaasPsiArquivoDTO;

import java.time.LocalDate;
import java.util.List;

public interface RaasPsiRepository {

    List<RaasPsiArquivoDTO> listarArquivos(LocalDate competencia, Long unidadeId, String unidade, int page, int size);

    long contarArquivos(LocalDate competencia, Long unidadeId, String unidade);
}
