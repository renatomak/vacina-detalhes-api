package br.gov.saude.vacinadetalhesapi.adapter;

import br.gov.saude.vacinadetalhesapi.dto.AtendimentoDTO;
import br.gov.saude.vacinadetalhesapi.mapper.ProntuarioMapper;
import br.gov.saude.vacinadetalhesapi.repository.ProntuarioRepositoryAdapter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProntuarioRepositoryAdapterImpl {

    @Autowired
    private ProntuarioRepositoryAdapter prontuarioRepositoryAdapter;

    @Autowired
    private ProntuarioMapper prontuarioMapper;

    public List<AtendimentoDTO> buscarAtendimentosComRegistrosPorPaciente(Long pacienteId) {
        var raws = prontuarioRepositoryAdapter.buscarProntuarioRawPorPaciente(pacienteId);
        var prontuario = prontuarioMapper.toProntuarioDTO(pacienteId, raws);
        return prontuario.atendimentos();
    }
}
