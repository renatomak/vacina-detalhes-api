package br.gov.saude.vacinadetalhesapi.mapper;

import br.gov.saude.vacinadetalhesapi.dto.EnderecoDTO;
import br.gov.saude.vacinadetalhesapi.dto.PacienteDTO;
import br.gov.saude.vacinadetalhesapi.dto.PacienteResumoDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class PacienteRowMapper {
    public RowMapper<PacienteDTO> detalhe() {
        return (rs, rowNum) -> new PacienteDTO(
                rs.getLong("id"),
                rs.getString("nome"),
                rs.getString("cpf"),
                PacienteRowMapper.converterSexo(rs.getString("sexo")),
                rs.getString("nome_mae"),
                rs.getString("nome_pai"),
                toLocalDate(rs.getObject("data_nascimento")),
                rs.getString("telefone"),
                new EnderecoDTO(
                        rs.getString("keyword"),
                        rs.getString("tipo_logradouro"),
                        rs.getString("logradouro"),
                        rs.getString("complemento"),
                        rs.getString("numero"),
                        rs.getString("cep"),
                        rs.getString("bairro"),
                        rs.getLong("cidade_id"),
                        rs.getString("cidade"),
                        rs.getString("uf")
                )
        );
    }

    public static String converterSexo(String sexo) {
        if (sexo == null) return "-";
        return switch (sexo.trim().toUpperCase()) {
            case "F" -> "FEMININO";
            case "M" -> "MASCULINO";
            default -> "-";
        };
    }

    public RowMapper<PacienteResumoDTO> resumo() {
        return (rs, rowNum) -> new PacienteResumoDTO(
                rs.getLong("id"),
                rs.getString("nome"),
                rs.getString("cpf"),
                toLocalDate(rs.getObject("data_nascimento"))
        );
    }

    private LocalDate toLocalDate(Object value) {
        if (value instanceof LocalDate localDate) {
            return localDate;
        }
        if (value instanceof java.sql.Date date) {
            return date.toLocalDate();
        }
        return null;
    }
}
