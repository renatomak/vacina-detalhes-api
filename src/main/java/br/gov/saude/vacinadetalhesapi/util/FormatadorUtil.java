package br.gov.saude.vacinadetalhesapi.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FormatadorUtil {
    public static String formatarData(LocalDate data) {
        if (data == null) return null;
        return DateTimeFormatter.ofPattern("dd/MM/yyyy").format(data);
    }
    public static String formatarCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) return cpf;
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }
    public static String formatarTelefone(String telefone) {
        if (telefone == null) return null;
        String digits = telefone.replaceAll("\\D", "");
        if (digits.length() == 11) {
            return digits.replaceAll("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
        } else if (digits.length() == 10) {
            return digits.replaceAll("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
        }
        return telefone;
    }
    public static String formatarCep(String cep) {
        if (cep == null || cep.length() != 8) return cep;
        return cep.replaceAll("(\\d{5})(\\d{3})", "$1-$2");
    }
}

