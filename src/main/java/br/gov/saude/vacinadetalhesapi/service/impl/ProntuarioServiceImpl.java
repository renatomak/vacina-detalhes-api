package br.gov.saude.vacinadetalhesapi.service.impl;

import br.gov.saude.vacinadetalhesapi.domain.ProntuarioItem;
import br.gov.saude.vacinadetalhesapi.port.ProntuarioRepositoryPort;
import br.gov.saude.vacinadetalhesapi.service.ProntuarioService;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProntuarioServiceImpl implements ProntuarioService {
    private final ProntuarioRepositoryPort prontuarioRepositoryPort;

    public ProntuarioServiceImpl(ProntuarioRepositoryPort prontuarioRepositoryPort) {
        this.prontuarioRepositoryPort = prontuarioRepositoryPort;
    }

    @Override
    public List<ProntuarioItem> buscarHistoricoPorPaciente(Long pacienteId) {
        List<ProntuarioItem> itens = prontuarioRepositoryPort.buscarHistoricoPorPaciente(pacienteId);
        return itens.stream()
                .map(item -> new ProntuarioItem(
                        item.dataRegistro(),
                        item.profissional(),
                        item.unidade(),
                        item.tipoRegistro(),
                        item.classificacaoRisco(),
                        limparHtml(item.conteudo())
                ))
                .collect(Collectors.toList());
    }

    private String limparHtml(String conteudo) {
        if (conteudo == null) return null;
        return Jsoup.parse(conteudo).text();
    }
}

