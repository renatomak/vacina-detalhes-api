package br.gov.saude.goiania.saude.api.adapter;

import br.gov.saude.goiania.saude.api.dto.AtendimentoDTO;
import br.gov.saude.goiania.saude.api.mapper.ProntuarioMapper;
import br.gov.saude.goiania.saude.api.repository.ProntuarioRepositoryAdapter;
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
