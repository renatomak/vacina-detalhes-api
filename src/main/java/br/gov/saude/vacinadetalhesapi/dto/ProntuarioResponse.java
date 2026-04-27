package br.gov.saude.vacinadetalhesapi.dto;

import br.gov.saude.vacinadetalhesapi.domain.ProntuarioItem;
import java.util.List;

public class ProntuarioResponse {
    private PacienteDTO paciente;
    private List<ProntuarioItem> atendimentos;

    public ProntuarioResponse() {}
    public ProntuarioResponse(PacienteDTO paciente, List<ProntuarioItem> atendimentos) {
        this.paciente = paciente;
        this.atendimentos = atendimentos;
    }
    public PacienteDTO getPaciente() { return paciente; }
    public void setPaciente(PacienteDTO paciente) { this.paciente = paciente; }
    public List<ProntuarioItem> getAtendimentos() { return atendimentos; }
    public void setAtendimentos(List<ProntuarioItem> atendimentos) { this.atendimentos = atendimentos; }
}
